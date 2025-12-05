package comk23cnt1.nvt.project3.nvt_service;

import comk23cnt1.nvt.project3.nvt_entity.NvtMeterReading;

import java.util.List;

public interface NvtMeterReadingService {
    List<NvtMeterReading> findAll();
    List<NvtMeterReading> findByRoom(Long roomId);

    NvtMeterReading create(NvtMeterReading reading);
    void delete(Long id);
}
