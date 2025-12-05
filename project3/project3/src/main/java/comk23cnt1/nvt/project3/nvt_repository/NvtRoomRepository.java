package comk23cnt1.nvt.project3.nvt_repository;

import comk23cnt1.nvt.project3.nvt_entity.NvtRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NvtRoomRepository extends JpaRepository<NvtRoom, Long> {

    boolean existsByRoomCode(String roomCode);
    boolean existsByRoomCodeAndIdNot(String roomCode, Long id);

    List<NvtRoom> findByStatus(NvtRoom.RoomStatus status);

    List<NvtRoom> findByStatusOrderByFloorAscRoomCodeAsc(NvtRoom.RoomStatus status);
}
