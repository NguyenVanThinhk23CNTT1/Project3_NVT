package comk23cnt1.nvt.project3.nvt_repository;

import comk23cnt1.nvt.project3.nvt_entity.NvtRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NvtRoomRepository extends JpaRepository<NvtRoom, Long> {

    boolean existsByRoomCode(String roomCode);
    boolean existsByRoomCodeAndIdNot(String roomCode, Long id);

    List<NvtRoom> findByStatus(NvtRoom.RoomStatus status);

    List<NvtRoom> findByStatusOrderByFloorAscRoomCodeAsc(NvtRoom.RoomStatus status);

    @Query("select r from NvtRoom r left join fetch r.hostel where r.id = :id")
    Optional<NvtRoom> findByIdWithHostel(@Param("id") Long id);

}
