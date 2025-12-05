package comk23cnt1.nvt.project3.nvt_repository;

import comk23cnt1.nvt.project3.nvt_entity.NvtMeterReading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NvtMeterReadingRepository extends JpaRepository<NvtMeterReading, Long> {

    boolean existsByRoomIdAndBillMonthAndBillYear(Long roomId, Integer billMonth, Integer billYear);

    Optional<NvtMeterReading> findByRoomIdAndBillMonthAndBillYear(Long roomId, Integer billMonth, Integer billYear);

    List<NvtMeterReading> findByRoomIdOrderByBillYearDescBillMonthDesc(Long roomId);

    Optional<NvtMeterReading> findTopByRoomIdOrderByBillYearDescBillMonthDesc(Long roomId);
}
