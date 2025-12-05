package com.nguyenvanthinh.k23cnt1.nvtrepository;

import com.nguyenvanthinh.k23cnt1.nvtentity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHouseId(Long houseId);
}
