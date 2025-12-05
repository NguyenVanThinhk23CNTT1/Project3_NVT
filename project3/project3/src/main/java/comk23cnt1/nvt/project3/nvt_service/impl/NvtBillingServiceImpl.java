package comk23cnt1.nvt.project3.nvt_service.impl;

import comk23cnt1.nvt.project3.nvt_entity.NvtBill;
import comk23cnt1.nvt.project3.nvt_entity.NvtContract;
import comk23cnt1.nvt.project3.nvt_entity.NvtMeterReading;
import comk23cnt1.nvt.project3.nvt_entity.NvtPayment;
import comk23cnt1.nvt.project3.nvt_repository.NvtBillRepository;
import comk23cnt1.nvt.project3.nvt_repository.NvtContractRepository;
import comk23cnt1.nvt.project3.nvt_repository.NvtMeterReadingRepository;
import comk23cnt1.nvt.project3.nvt_repository.NvtPaymentRepository;
import comk23cnt1.nvt.project3.nvt_service.NvtBillingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class NvtBillingServiceImpl implements NvtBillingService {

    private final NvtBillRepository billRepo;
    private final NvtPaymentRepository paymentRepo;
    private final NvtContractRepository contractRepo;
    private final NvtMeterReadingRepository meterRepo;

    // Giá điện/nước demo (sau này đọc từ services table)
    private static final BigDecimal ELECTRIC_PRICE = new BigDecimal("3500"); // VND/kWh
    private static final BigDecimal WATER_PRICE = new BigDecimal("15000");   // VND/m3

    public NvtBillingServiceImpl(NvtBillRepository billRepo,
                                 NvtPaymentRepository paymentRepo,
                                 NvtContractRepository contractRepo,
                                 NvtMeterReadingRepository meterRepo) {
        this.billRepo = billRepo;
        this.paymentRepo = paymentRepo;
        this.contractRepo = contractRepo;
        this.meterRepo = meterRepo;
    }

    @Override
    public List<NvtBill> findAllBills() {
        return billRepo.findAll();
    }

    @Override
    public NvtBill findBill(Long billId) {
        return billRepo.findById(billId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn ID=" + billId));
    }

    @Transactional
    @Override
    public NvtBill createBill(Long contractId, Integer month, Integer year) {
        if (contractId == null) throw new IllegalArgumentException("Chưa chọn hợp đồng");
        if (month == null || month < 1 || month > 12) throw new IllegalArgumentException("Tháng không hợp lệ");
        if (year == null || year < 2000) throw new IllegalArgumentException("Năm không hợp lệ");

        if (billRepo.existsByContractIdAndBillMonthAndBillYear(contractId, month, year)) {
            throw new IllegalArgumentException("Hợp đồng này đã có hóa đơn tháng/năm này");
        }

        NvtContract c = contractRepo.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hợp đồng"));

        if (c.getStatus() != NvtContract.ContractStatus.ACTIVE) {
            throw new IllegalArgumentException("Chỉ tạo hóa đơn khi hợp đồng ACTIVE");
        }

        // ✅ LẤY METER THEO roomId + month + year (đúng như bạn yêu cầu)
        NvtMeterReading meter = meterRepo.findByRoomIdAndBillMonthAndBillYear(c.getRoomId(), month, year)
                .orElseThrow(() -> new IllegalArgumentException("Chưa có chỉ số điện nước cho phòng tháng/năm này"));

        int eUsed = meter.getElectricNew() - meter.getElectricOld();
        int wUsed = meter.getWaterNew() - meter.getWaterOld();

        if (eUsed < 0) throw new IllegalArgumentException("Chỉ số điện không hợp lệ (mới < cũ)");
        if (wUsed < 0) throw new IllegalArgumentException("Chỉ số nước không hợp lệ (mới < cũ)");

        BigDecimal rent = c.getRentPrice() == null ? BigDecimal.ZERO : c.getRentPrice();
        BigDecimal electric = ELECTRIC_PRICE.multiply(BigDecimal.valueOf(eUsed));
        BigDecimal water = WATER_PRICE.multiply(BigDecimal.valueOf(wUsed));

        BigDecimal service = BigDecimal.ZERO;  // để sau: cộng dịch vụ cố định
        BigDecimal discount = BigDecimal.ZERO; // để sau: giảm giá

        BigDecimal total = rent.add(electric).add(water).add(service).subtract(discount);

        NvtBill bill = new NvtBill();
        bill.setContractId(c.getId());
        bill.setRoomId(c.getRoomId());
        bill.setBillMonth(month);
        bill.setBillYear(year);

        bill.setRentAmount(rent);
        bill.setElectricAmount(electric);
        bill.setWaterAmount(water);
        bill.setServiceAmount(service);
        bill.setDiscount(discount);
        bill.setTotalAmount(total);

        // ví dụ hạn thanh toán: ngày 10 của tháng đó
        bill.setDueDate(LocalDate.of(year, month, 10));
        bill.setStatus(NvtBill.BillStatus.UNPAID);

        return billRepo.save(bill);
    }

    @Override
    public List<NvtPayment> payments(Long billId) {
        return paymentRepo.findByBillIdOrderByPaidAtDesc(billId);
    }

    @Transactional
    @Override
    public NvtPayment addPayment(Long billId, NvtPayment payment) {
        NvtBill bill = findBill(billId);

        if (payment.getAmount() == null || payment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Số tiền thanh toán phải > 0");
        }

        payment.setBillId(billId);
        NvtPayment saved = paymentRepo.save(payment);

        BigDecimal sumPaid = paymentRepo.findByBillIdOrderByPaidAtDesc(billId).stream()
                .filter(p -> p.getStatus() == NvtPayment.PayStatus.SUCCESS)
                .map(NvtPayment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (sumPaid.compareTo(bill.getTotalAmount()) >= 0) {
            bill.setStatus(NvtBill.BillStatus.PAID);
            billRepo.save(bill);
        }

        return saved;
    }
}
