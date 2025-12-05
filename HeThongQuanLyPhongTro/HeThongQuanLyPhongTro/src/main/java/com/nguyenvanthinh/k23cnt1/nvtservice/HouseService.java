package com.nguyenvanthinh.k23cnt1.nvtservice;

import com.nguyenvanthinh.k23cnt1.nvtentity.House;
import com.nguyenvanthinh.k23cnt1.nvtrepository.HouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HouseService {

    private final HouseRepository houseRepository;

    // Lấy toàn bộ nhà trọ
    public List<House> getAll() {
        return houseRepository.findAll();
    }

    // Lấy nhà trọ theo ID
    public House getById(Long id) {
        return houseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("House not found with ID: " + id));
    }

    // Lưu hoặc cập nhật nhà trọ
    public void save(House house) {
        houseRepository.save(house);
    }

    // Xóa nhà trọ
    public void delete(Long id) {
        houseRepository.deleteById(id);
    }
}
