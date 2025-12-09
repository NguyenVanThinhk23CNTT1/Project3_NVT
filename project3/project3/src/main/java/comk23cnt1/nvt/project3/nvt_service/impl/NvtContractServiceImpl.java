package comk23cnt1.nvt.project3.nvt_service.impl;

import comk23cnt1.nvt.project3.nvt_entity.NvtContract;
import comk23cnt1.nvt.project3.nvt_entity.NvtContractMember;
import comk23cnt1.nvt.project3.nvt_entity.NvtRoom;
import comk23cnt1.nvt.project3.nvt_repository.NvtContractMemberRepository;
import comk23cnt1.nvt.project3.nvt_repository.NvtContractRepository;
import comk23cnt1.nvt.project3.nvt_repository.NvtRoomRepository;
import comk23cnt1.nvt.project3.nvt_service.NvtContractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class NvtContractServiceImpl implements NvtContractService {

    private final NvtContractRepository contractRepo;
    private final NvtContractMemberRepository memberRepo;

    // ✅ NEW: để lấy rent/deposit theo phòng + update room.status
    private final NvtRoomRepository roomRepo;

    public NvtContractServiceImpl(NvtContractRepository contractRepo,
                                  NvtContractMemberRepository memberRepo,
                                  NvtRoomRepository roomRepo) {
        this.contractRepo = contractRepo;
        this.memberRepo = memberRepo;
        this.roomRepo = roomRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<NvtContract> findAll() {
        return contractRepo.findAllByOrderByIdDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public NvtContract findById(Long id) {
        return contractRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hợp đồng"));
    }

    private NvtRoom requireRoom(Long roomId) {
        return roomRepo.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phòng ID=" + roomId));
    }

    @Override
    public NvtContract create(NvtContract c) {
        if (c == null) throw new IllegalArgumentException("Dữ liệu hợp đồng không hợp lệ");
        if (c.getRoomId() == null) throw new IllegalArgumentException("Chưa chọn phòng");
        if (c.getTenantId() == null) throw new IllegalArgumentException("Chưa chọn người đại diện");
        if (c.getStartDate() == null) throw new IllegalArgumentException("Chưa nhập ngày bắt đầu");

        // ✅ Chặn tạo ACTIVE mới nếu phòng đã có ACTIVE
        if (contractRepo.existsByRoomIdAndStatus(c.getRoomId(), NvtContract.ContractStatus.ACTIVE)) {
            throw new IllegalArgumentException("Phòng này đang có hợp đồng ACTIVE. Hãy kết thúc hợp đồng cũ trước.");
        }

        NvtRoom room = requireRoom(c.getRoomId());

        // ✅ Bỏ trống => lấy theo phòng
        if (c.getRentPrice() == null) c.setRentPrice(room.getRentPrice());
        if (c.getDeposit() == null) c.setDeposit(room.getDepositDefault() != null ? room.getDepositDefault() : BigDecimal.ZERO);

        if (c.getRentPrice() == null) c.setRentPrice(BigDecimal.ZERO);
        if (c.getDeposit() == null) c.setDeposit(BigDecimal.ZERO);

        if (c.getStatus() == null) c.setStatus(NvtContract.ContractStatus.ACTIVE);

        if (c.getContractCode() == null || c.getContractCode().trim().isBlank()) {
            c.setContractCode(genCode());
        } else {
            c.setContractCode(c.getContractCode().trim());
        }

        if (contractRepo.existsByContractCode(c.getContractCode())) {
            c.setContractCode(genCode());
        }

        if (c.getEndDate() != null && c.getEndDate().isBefore(c.getStartDate())) {
            throw new IllegalArgumentException("Ngày kết thúc không được trước ngày bắt đầu");
        }

        // ✅ Đồng bộ phòng: có hợp đồng ACTIVE => room RENTING
        room.setStatus(NvtRoom.RoomStatus.RENTING);
        roomRepo.save(room);

        NvtContract saved = contractRepo.save(c);

        // Auto add đại diện vào members (primary) nếu chưa có
        if (!memberRepo.existsByContractIdAndTenantId(saved.getId(), saved.getTenantId())) {
            NvtContractMember primary = new NvtContractMember();
            primary.setContractId(saved.getId());
            primary.setTenantId(saved.getTenantId());
            primary.setIsPrimary(true);
            primary.setMoveInDate(saved.getStartDate() != null ? saved.getStartDate() : LocalDate.now());
            primary.setMoveOutDate(null);
            primary.setRelation("Đại diện");
            memberRepo.save(primary);
        } else {
            // nếu đã có (do dữ liệu cũ), đảm bảo primary chưa bị moveOut khi vừa tạo
            memberRepo.findByContractIdAndTenantId(saved.getId(), saved.getTenantId())
                    .ifPresent(m -> {
                        m.setIsPrimary(true);
                        m.setMoveOutDate(null);
                        if (m.getMoveInDate() == null) m.setMoveInDate(saved.getStartDate());
                        if (m.getRelation() == null || m.getRelation().isBlank()) m.setRelation("Đại diện");
                        memberRepo.save(m);
                    });
        }

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<NvtContractMember> members(Long contractId) {
        if (contractId == null) throw new IllegalArgumentException("Thiếu contractId");
        return memberRepo.findByContractIdOrderByTenantIdAsc(contractId);
    }

    @Override
    public void addMember(Long contractId, NvtContractMember m) {
        if (contractId == null) throw new IllegalArgumentException("Thiếu contractId");
        if (m == null) throw new IllegalArgumentException("Dữ liệu người ở ghép không hợp lệ");
        if (m.getTenantId() == null) throw new IllegalArgumentException("Chưa chọn người thuê");

        NvtContract contract = findById(contractId);
        if (contract.getStatus() != NvtContract.ContractStatus.ACTIVE) {
            throw new IllegalArgumentException("Hợp đồng không còn ACTIVE nên không thể thêm người");
        }

        var existed = memberRepo.findByContractIdAndTenantId(contractId, m.getTenantId());
        if (existed.isPresent()) {
            NvtContractMember ex = existed.get();
            if (ex.getMoveOutDate() == null) {
                throw new IllegalArgumentException("Người này đã có trong hợp đồng");
            }
            ex.setMoveOutDate(null);
            ex.setMoveInDate(m.getMoveInDate() != null ? m.getMoveInDate() : LocalDate.now());
            if (m.getRelation() != null) ex.setRelation(m.getRelation());
            memberRepo.save(ex);
            return;
        }

        m.setContractId(contractId);
        if (m.getIsPrimary() == null) m.setIsPrimary(false);

        if (m.getMoveInDate() == null) {
            m.setMoveInDate(contract.getStartDate() != null ? contract.getStartDate() : LocalDate.now());
        }

        if (Boolean.TRUE.equals(m.getIsPrimary())) {
            throw new IllegalArgumentException("Không cho thêm primary mới. Đại diện đã được set khi tạo hợp đồng.");
        }

        m.setMoveOutDate(null);
        memberRepo.save(m);
    }

    @Override
    public void removeMember(Long contractId, Long tenantId) {
        if (contractId == null) throw new IllegalArgumentException("Thiếu contractId");
        if (tenantId == null) throw new IllegalArgumentException("Thiếu tenantId");

        NvtContract contract = findById(contractId);

        NvtContractMember cm = memberRepo.findByContractIdAndTenantId(contractId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người này trong hợp đồng"));

        if (cm.getMoveOutDate() != null) {
            throw new IllegalArgumentException("Người này đã rời phòng trước đó");
        }

        boolean isRepresentative = contract.getTenantId() != null && contract.getTenantId().equals(tenantId);

        // chỉ cấm đại diện rời khi hợp đồng còn ACTIVE
        if (isRepresentative && contract.getStatus() == NvtContract.ContractStatus.ACTIVE) {
            throw new IllegalArgumentException("Không thể cho người đại diện rời phòng khi hợp đồng còn ACTIVE. (Muốn đổi đại diện cần chức năng nâng cấp).");
        }

        cm.setMoveOutDate(LocalDate.now());
        memberRepo.save(cm);
    }

    @Override
    public void endContract(Long contractId) {
        NvtContract c = findById(contractId);

        if (c.getStatus() == NvtContract.ContractStatus.ENDED) return;
        if (c.getStatus() == NvtContract.ContractStatus.CANCELLED) {
            throw new IllegalArgumentException("Hợp đồng đã CANCELLED nên không thể end");
        }

        c.setStatus(NvtContract.ContractStatus.ENDED);
        if (c.getEndDate() == null) c.setEndDate(LocalDate.now());
        contractRepo.save(c);

        // ✅ Đồng bộ phòng: END => room EMPTY
        if (c.getRoomId() != null) {
            NvtRoom room = requireRoom(c.getRoomId());
            room.setStatus(NvtRoom.RoomStatus.EMPTY);
            roomRepo.save(room);
        }

        // Chốt luôn danh sách ở ghép: ai chưa có ngày ra thì set ngày ra = endDate
        LocalDate out = c.getEndDate();
        List<NvtContractMember> ms = memberRepo.findByContractIdOrderByTenantIdAsc(contractId);
        for (NvtContractMember m : ms) {
            if (m.getMoveOutDate() == null) {
                m.setMoveOutDate(out);
                memberRepo.save(m);
            }
        }
    }

    @Override
    public void resumeContract(Long contractId) {
        NvtContract c = findById(contractId);

        if (c.getStatus() == NvtContract.ContractStatus.ACTIVE) return;
        if (c.getStatus() == NvtContract.ContractStatus.CANCELLED) {
            throw new IllegalArgumentException("Hợp đồng đã CANCELLED nên không thể tiếp tục");
        }

        // ✅ Chặn resume nếu phòng đang có ACTIVE khác
        if (c.getRoomId() != null && contractRepo.existsByRoomIdAndStatus(c.getRoomId(), NvtContract.ContractStatus.ACTIVE)) {
            throw new IllegalArgumentException("Phòng này đang có hợp đồng ACTIVE khác. Không thể tiếp tục hợp đồng này.");
        }

        c.setStatus(NvtContract.ContractStatus.ACTIVE);
        c.setEndDate(null);
        contractRepo.save(c);

        // ✅ Đồng bộ phòng: RESUME => room RENTING
        if (c.getRoomId() != null) {
            NvtRoom room = requireRoom(c.getRoomId());
            room.setStatus(NvtRoom.RoomStatus.RENTING);
            roomRepo.save(room);
        }

        // đảm bảo đại diện vẫn đang "ở trong phòng"
        if (c.getTenantId() != null) {
            memberRepo.findByContractIdAndTenantId(contractId, c.getTenantId())
                    .ifPresent(m -> {
                        m.setIsPrimary(true);
                        m.setMoveOutDate(null);
                        if (m.getMoveInDate() == null) m.setMoveInDate(LocalDate.now());
                        if (m.getRelation() == null || m.getRelation().isBlank()) m.setRelation("Đại diện");
                        memberRepo.save(m);
                    });
        }
    }

    private String genCode() {
        return "CT-" + UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
    }
}
