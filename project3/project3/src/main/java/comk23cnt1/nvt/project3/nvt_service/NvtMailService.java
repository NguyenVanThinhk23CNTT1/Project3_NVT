package comk23cnt1.nvt.project3.nvt_service;

import comk23cnt1.nvt.project3.nvt_entity.NvtBill;

import java.math.BigDecimal;

public interface NvtMailService {
    void sendPaymentSuccessEmail(String toEmail, String subject, NvtBill bill, BigDecimal paidAmount);
}
