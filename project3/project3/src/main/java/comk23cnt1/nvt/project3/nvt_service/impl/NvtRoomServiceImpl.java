package comk23cnt1.nvt.project3.nvt_service.impl;

import comk23cnt1.nvt.project3.nvt_entity.NvtHostel;
import comk23cnt1.nvt.project3.nvt_entity.NvtRoom;
import comk23cnt1.nvt.project3.nvt_repository.NvtHostelRepository;
import comk23cnt1.nvt.project3.nvt_repository.NvtRoomRepository;
import comk23cnt1.nvt.project3.nvt_service.NvtRoomService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class NvtRoomServiceImpl implements NvtRoomService {

    private final NvtRoomRepository roomRepository;
    private final NvtHostelRepository hostelRepository;

    private static final Path ROOM_UPLOAD_DIR = Paths.get("uploads", "rooms")
            .toAbsolutePath().normalize();

    private static final Set<String> ALLOWED_EXT = Set.of("jpg", "jpeg", "png", "webp", "gif");

    public NvtRoomServiceImpl(NvtRoomRepository roomRepository,
                              NvtHostelRepository hostelRepository) {
        this.roomRepository = roomRepository;
        this.hostelRepository = hostelRepository;
    }

    @Override
    public List<NvtRoom> findAll() {
        return roomRepository.findAll();
    }

    @Override
    public NvtRoom findById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phòng ID=" + id));
    }

    @Override
    public NvtRoom create(NvtRoom room, MultipartFile imageFile) {
        if (room == null) throw new IllegalArgumentException("Dữ liệu phòng không hợp lệ");
        normalize(room);

        if (room.getRoomCode() == null || room.getRoomCode().isBlank())
            throw new IllegalArgumentException("Mã phòng không được rỗng");

        if (roomRepository.existsByRoomCode(room.getRoomCode()))
            throw new IllegalArgumentException("Mã phòng đã tồn tại");

        if (room.getRentPrice() == null || room.getRentPrice().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Giá thuê phải > 0");

        // ✅ bắt buộc chọn nhà trọ
        Long hostelId = (room.getHostel() != null) ? room.getHostel().getId() : null;
        if (hostelId == null)
            throw new IllegalArgumentException("Vui lòng chọn nhà trọ");

        NvtHostel hostel = hostelRepository.findById(hostelId)
                .orElseThrow(() -> new IllegalArgumentException("Nhà trọ không tồn tại: ID=" + hostelId));
        room.setHostel(hostel);

        if (room.getStatus() == null) room.setStatus(NvtRoom.RoomStatus.EMPTY);
        if (room.getFloor() == null) room.setFloor(1);
        if (room.getMaxPeople() == null) room.setMaxPeople(2);
        if (room.getDepositDefault() == null) room.setDepositDefault(BigDecimal.ZERO);

        // save hình (nếu có)
        if (imageFile != null && !imageFile.isEmpty()) {
            room.setImageUrl(saveRoomImage(imageFile));
        }

        return roomRepository.save(room);
    }

    @Override
    public NvtRoom update(Long id, NvtRoom room, MultipartFile imageFile) {
        NvtRoom existing = findById(id);
        if (room == null) throw new IllegalArgumentException("Dữ liệu phòng không hợp lệ");
        normalize(room);

        if (room.getRoomCode() == null || room.getRoomCode().isBlank())
            throw new IllegalArgumentException("Mã phòng không được rỗng");

        if (roomRepository.existsByRoomCodeAndIdNot(room.getRoomCode(), id))
            throw new IllegalArgumentException("Mã phòng đã tồn tại (trùng phòng khác)");

        if (room.getRentPrice() == null || room.getRentPrice().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Giá thuê phải > 0");

        // ✅ bắt buộc chọn nhà trọ
        Long hostelId = (room.getHostel() != null) ? room.getHostel().getId() : null;
        if (hostelId == null)
            throw new IllegalArgumentException("Vui lòng chọn nhà trọ");

        NvtHostel hostel = hostelRepository.findById(hostelId)
                .orElseThrow(() -> new IllegalArgumentException("Nhà trọ không tồn tại: ID=" + hostelId));
        existing.setHostel(hostel);

        existing.setRoomCode(room.getRoomCode());
        existing.setRoomName(room.getRoomName());
        existing.setFloor(room.getFloor() == null ? 1 : room.getFloor());
        existing.setArea(room.getArea());
        existing.setMaxPeople(room.getMaxPeople() == null ? 2 : room.getMaxPeople());
        existing.setRentPrice(room.getRentPrice());
        existing.setDepositDefault(room.getDepositDefault() == null ? BigDecimal.ZERO : room.getDepositDefault());
        existing.setStatus(room.getStatus() == null ? NvtRoom.RoomStatus.EMPTY : room.getStatus());
        existing.setDescription(room.getDescription());

        if (imageFile != null && !imageFile.isEmpty()) {
            existing.setImageUrl(saveRoomImage(imageFile));
        }

        return roomRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!roomRepository.existsById(id))
            throw new IllegalArgumentException("Không tìm thấy phòng để xóa: ID=" + id);

        roomRepository.deleteById(id);
    }

    private void normalize(NvtRoom room) {
        if (room.getRoomCode() != null) room.setRoomCode(room.getRoomCode().trim());
        if (room.getRoomName() != null) {
            String n = room.getRoomName().trim();
            room.setRoomName(n.isBlank() ? null : n);
        }
        if (room.getDescription() != null) {
            String d = room.getDescription().trim();
            room.setDescription(d.isBlank() ? null : d);
        }
    }

    private String saveRoomImage(MultipartFile file) {
        try {
            if (file.getSize() > 5 * 1024 * 1024) {
                throw new IllegalArgumentException("Ảnh quá lớn (tối đa 5MB)");
            }

            String original = file.getOriginalFilename() == null ? "" : file.getOriginalFilename();
            String ext = getExtension(original).toLowerCase();

            if (!ALLOWED_EXT.contains(ext)) {
                throw new IllegalArgumentException("Chỉ nhận ảnh: jpg, jpeg, png, webp, gif");
            }

            Files.createDirectories(ROOM_UPLOAD_DIR);

            String safeName = UUID.randomUUID().toString().replace("-", "")
                    + "_" + System.currentTimeMillis() + "." + ext;

            Path target = ROOM_UPLOAD_DIR.resolve(safeName).normalize();
            try (var in = file.getInputStream()) {
                Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            }

            return "/uploads/rooms/" + safeName;

        } catch (IOException e) {
            throw new RuntimeException("Upload ảnh thất bại: " + e.getMessage(), e);
        }
    }

    private String getExtension(String filename) {
        int dot = filename.lastIndexOf('.');
        if (dot < 0 || dot == filename.length() - 1) return "";
        return filename.substring(dot + 1);
    }
}
