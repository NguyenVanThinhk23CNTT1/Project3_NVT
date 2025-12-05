package comk23cnt1.nvt.project3.nvt_service.impl;

import comk23cnt1.nvt.project3.nvt_entity.NvtContract;
import comk23cnt1.nvt.project3.nvt_entity.NvtContractMember;
import comk23cnt1.nvt.project3.nvt_repository.NvtContractMemberRepository;
import comk23cnt1.nvt.project3.nvt_repository.NvtContractRepository;
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

    public NvtContractServiceImpl(NvtContractRepository contractRepo,
                                  NvtContractMemberRepository memberRepo) {
        this.contractRepo = contractRepo;
        this.memberRepo = memberRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<NvtContract> findAll() {
        // ưu tiên sort mới nhất
        return contractRepo.findAllByOrderByIdDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public NvtContract findById(Long id) {
        return contractRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hợp đồng"));
    }

    @Override
    public NvtContract create(NvtContract c) {
        if (c == null) throw new IllegalArgumentException("Dữ liệu hợp đồng không hợp lệ");
        if (c.getRoomId() == null) throw new IllegalArgumentException("Chưa chọn phòng");
        if (c.getTenantId() == null) throw new IllegalArgumentException("Chưa chọn người đại diện");
        if (c.getStartDate() == null) throw new IllegalArgumentException("Chưa nhập ngày bắt đầu");
        if (c.getRentPrice() == null) throw new IllegalArgumentException("Chưa nhập giá thuê");

        if (c.getDeposit() == null) c.setDeposit(BigDecimal.ZERO);
        if (c.getStatus() == null) c.setStatus(NvtContract.ContractStatus.ACTIVE);

        // auto contractCode nếu chưa có
        if (c.getContractCode() == null || c.getContractCode().trim().isBlank()) {
            c.setContractCode(genCode());
        } else {
            c.setContractCode(c.getContractCode().trim());
        }

        if (contractRepo.existsByContractCode(c.getContractCode())) {
            // tránh trùng code, generate lại
            c.setContractCode(genCode());
        }

        // validate endDate
        if (c.getEndDate() != null && c.getEndDate().isBefore(c.getStartDate())) {
            throw new IllegalArgumentException("Ngày kết thúc không được trước ngày bắt đầu");
        }

        NvtContract saved = contractRepo.save(c);

        // auto add đại diện vào members (is_primary=1)
        NvtContractMember primary = new NvtContractMember();
        primary.setContractId(saved.getId());
        primary.setTenantId(saved.getTenantId());
        primary.setIsPrimary(true);
        primary.setMoveInDate(c.getStartDate() != null ? c.getStartDate() : LocalDate.now());
        primary.setRelation("Đại diện");
        if (!memberRepo.existsByContractIdAndTenantId(saved.getId(), saved.getTenantId())) {
            memberRepo.save(primary);
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

        if (memberRepo.existsByContractIdAndTenantId(contractId, m.getTenantId())) {
            throw new IllegalArgumentException("Người này đã có trong hợp đồng");
        }

        // set keys
        m.setContractId(contractId);

        if (m.getIsPrimary() == null) m.setIsPrimary(false);

        // default moveInDate
        if (m.getMoveInDate() == null) {
            m.setMoveInDate(contract.getStartDate() != null ? contract.getStartDate() : LocalDate.now());
        }

        // không cho set primary nếu đã có primary khác (để đơn giản)
        if (Boolean.TRUE.equals(m.getIsPrimary())) {
            // nếu muốn đổi primary thì phải xử lý thêm, ở đây chặn luôn cho chắc
            throw new IllegalArgumentException("Không cho thêm primary mới. Đại diện đã được set khi tạo hợp đồng.");
        }

        memberRepo.save(m);
    }

    @Override
    public void removeMember(Long contractId, Long tenantId) {
        if (contractId == null) throw new IllegalArgumentException("Thiếu contractId");
        if (tenantId == null) throw new IllegalArgumentException("Thiếu tenantId");

        NvtContract contract = findById(contractId);

        // không cho remove đại diện
        if (contract.getTenantId() != null && contract.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException("Không thể cho người đại diện rời phòng. Hãy đổi đại diện trước (nâng cấp sau).");
        }

        NvtContractMember cm = memberRepo.findByContractIdAndTenantId(contractId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người này trong hợp đồng"));

        // set move_out_date (soft logic) rồi xóa luôn theo yêu cầu hiện tại (đơn giản)
        // Nếu muốn lưu lịch sử thì đừng delete, chỉ update moveOutDate.
        memberRepo.delete(cm);
    }

    @Override
    public void endContract(Long contractId) {
        NvtContract c = findById(contractId);
        if (c.getStatus() == NvtContract.ContractStatus.ENDED) return;

        c.setStatus(NvtContract.ContractStatus.ENDED);
        if (c.getEndDate() == null) c.setEndDate(LocalDate.now());
        contractRepo.save(c);
    }

    private String genCode() {
        // VD: CT-8F3A1B
        return "CT-" + UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
    }
}
