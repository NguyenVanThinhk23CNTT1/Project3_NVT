package comk23cnt1.nvt.project3.nvt_service.impl;

import comk23cnt1.nvt.project3.nvt_entity.NvtBill;
import comk23cnt1.nvt.project3.nvt_service.NvtMailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

@Service
public class NvtMailServiceImpl implements NvtMailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from:}")
    private String from;

    public NvtMailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendPaymentSuccessEmail(String toEmail, String subject, NvtBill bill, BigDecimal paidAmount) {
        try {
            var msg = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(msg, true, StandardCharsets.UTF_8.name());

            if (from != null && !from.isBlank()) helper.setFrom(from);
            helper.setTo(toEmail);
            helper.setSubject(subject);

            String html = buildHtml(bill, paidAmount);
            helper.setText(html, true);

            mailSender.send(msg);
        } catch (Exception e) {
            // Không chặn luồng thanh toán chỉ vì mail fail
            // Bạn có thể log ra nếu muốn
            System.out.println("Send mail failed: " + e.getMessage());
        }
    }

    private String buildHtml(NvtBill b, BigDecimal paidAmount) {
        return """
            <div style="font-family:Arial,sans-serif;line-height:1.5">
              <h2>✅ Thanh toán hóa đơn thành công</h2>
              <p>Cảm ơn bạn! Hệ thống đã ghi nhận thanh toán.</p>
              <div style="padding:12px;border:1px solid #eee;border-radius:12px;background:#fafafa">
                <p><b>Hóa đơn:</b> %s/%s</p>
                <p><b>Phòng ID:</b> %s</p>
                <p><b>Số tiền thanh toán:</b> %s</p>
                <p><b>Tổng hóa đơn:</b> %s</p>
                <p><b>Trạng thái:</b> %s</p>
              </div>
              <p style="color:#666;margin-top:12px">Phòngtốt • Biên nhận tự động</p>
            </div>
        """.formatted(
                b.getBillMonth(), b.getBillYear(),
                b.getRoomId(),
                paidAmount,
                b.getTotalAmount(),
                b.getStatus()
        );
    }
}
