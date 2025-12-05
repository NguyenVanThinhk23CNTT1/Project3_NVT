package comk23cnt1.nvt.project3.nvt_service.impl;

import comk23cnt1.nvt.project3.nvt_entity.NvtMeterReading;
import comk23cnt1.nvt.project3.nvt_repository.NvtMeterReadingRepository;
import comk23cnt1.nvt.project3.nvt_service.NvtMeterReadingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NvtMeterReadingServiceImpl implements NvtMeterReadingService {

    private final NvtMeterReadingRepository repo;

    public NvtMeterReadingServiceImpl(NvtMeterReadingRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<NvtMeterReading> findAll() {
        return repo.findAll();
    }

    @Override
    public List<NvtMeterReading> findByRoom(Long roomId) {
        return repo.findByRoomIdOrderByBillYearDescBillMonthDesc(roomId);
    }

    @Override
    public NvtMeterReading create(NvtMeterReading r) {
        if (r.getRoomId() == null) throw new IllegalArgumentException("Chưa chọn phòng");
        if (r.getBillMonth() == null || r.getBillMonth() < 1 || r.getBillMonth() > 12)
            throw new IllegalArgumentException("Tháng không hợp lệ (1-12)");
        if (r.getBillYear() == null || r.getBillYear() < 2000)
            throw new IllegalArgumentException("Năm không hợp lệ");

        if (repo.existsByRoomIdAndBillMonthAndBillYear(r.getRoomId(), r.getBillMonth(), r.getBillYear()))
            throw new IllegalArgumentException("Phòng này đã có chỉ số cho tháng/năm này");

        // Nếu không nhập old -> tự lấy new của kỳ gần nhất
        if (r.getElectricOld() == null || r.getWaterOld() == null) {
            repo.findTopByRoomIdOrderByBillYearDescBillMonthDesc(r.getRoomId()).ifPresent(prev -> {
                if (r.getElectricOld() == null) r.setElectricOld(prev.getElectricNew());
                if (r.getWaterOld() == null) r.setWaterOld(prev.getWaterNew());
            });
            if (r.getElectricOld() == null) r.setElectricOld(0);
            if (r.getWaterOld() == null) r.setWaterOld(0);
        }

        if (r.getElectricNew() == null) r.setElectricNew(r.getElectricOld());
        if (r.getWaterNew() == null) r.setWaterNew(r.getWaterOld());

        if (r.getElectricNew() < r.getElectricOld())
            throw new IllegalArgumentException("Chỉ số điện mới phải >= chỉ số điện cũ");
        if (r.getWaterNew() < r.getWaterOld())
            throw new IllegalArgumentException("Chỉ số nước mới phải >= chỉ số nước cũ");

        return repo.save(r);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new IllegalArgumentException("Không tìm thấy bản ghi để xóa: ID=" + id);
        repo.deleteById(id);
    }
}
