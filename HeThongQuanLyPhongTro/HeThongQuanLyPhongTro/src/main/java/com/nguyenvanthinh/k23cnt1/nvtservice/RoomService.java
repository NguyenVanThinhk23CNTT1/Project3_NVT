package com.nguyenvanthinh.k23cnt1.nvtservice;

import com.nguyenvanthinh.k23cnt1.nvtentity.Room;
import com.nguyenvanthinh.k23cnt1.nvtrepository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    // Lấy danh sách tất cả phòng
    public List<Room> getAll() {
        return roomRepository.findAll();
    }

    // Lấy phòng theo ID
    public Room getById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with ID: " + id));
    }

    // Lưu hoặc cập nhật phòng
    public void save(Room room) {
        roomRepository.save(room);
    }

    // Xóa phòng
    public void delete(Long id) {
        roomRepository.deleteById(id);
    }

    // Lấy tất cả phòng thuộc 1 nhà trọ
    public List<Room> getByHouse(Long houseId) {
        return roomRepository.findByHouseId(houseId);
    }
}
