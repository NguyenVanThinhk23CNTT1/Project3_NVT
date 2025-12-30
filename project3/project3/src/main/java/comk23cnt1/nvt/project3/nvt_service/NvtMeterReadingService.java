package comk23cnt1.nvt.project3.nvt_service;

import comk23cnt1.nvt.project3.nvt_entity.NvtMeterReading;

import java.util.List;
import java.util.Optional;

public interface NvtMeterReadingService {
    List<NvtMeterReading> findAll();

    List<NvtMeterReading> findByRoom(Long roomId);

    // Tìm chỉ số theo phòng, tháng, năm
    Optional<NvtMeterReading> findByRoomAndMonthAndYear(Long roomId, Integer month, Integer year);

    NvtMeterReading create(NvtMeterReading reading);

    void delete(Long id);
}
