package comk23cnt1.nvt.project3.nvt_controller;

import comk23cnt1.nvt.project3.nvt_entity.NvtPayment;
import comk23cnt1.nvt.project3.nvt_service.NvtBillingService;
import comk23cnt1.nvt.project3.nvt_service.NvtContractService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/admin/bills")
public class NvtAdminBillingController {

    private final NvtBillingService billingService;
    private final NvtContractService contractService;

    public NvtAdminBillingController(NvtBillingService billingService,
                                     NvtContractService contractService) {
        this.billingService = billingService;
        this.contractService = contractService;
    }

    @GetMapping
    public String bills(Model model,
                        @RequestParam(value="msg", required=false) String msg,
                        @RequestParam(value="err", required=false) String err) {
        model.addAttribute("bills", billingService.findAllBills());
        model.addAttribute("contracts", contractService.findAll());
        model.addAttribute("msg", msg);
        model.addAttribute("err", err);
        return "admin/bills";
    }

    @PostMapping("/create")
    public String create(@RequestParam Long contractId,
                         @RequestParam Integer billMonth,
                         @RequestParam Integer billYear) {
        try {
            billingService.createBill(contractId, billMonth, billYear);
            return "redirect:/admin/bills?msg=" + enc("Tạo hóa đơn thành công");
        } catch (Exception e) {
            return "redirect:/admin/bills?err=" + enc(e.getMessage());
        }
    }

    @GetMapping("/{billId}")
    public String billDetail(@PathVariable Long billId, Model model,
                             @RequestParam(value="msg", required=false) String msg,
                             @RequestParam(value="err", required=false) String err) {
        model.addAttribute("bill", billingService.findBill(billId));
        model.addAttribute("payments", billingService.payments(billId));
        model.addAttribute("newPayment", new NvtPayment());
        model.addAttribute("msg", msg);
        model.addAttribute("err", err);
        return "admin/bill-detail";
    }

    @PostMapping("/{billId}/pay")
    public String pay(@PathVariable Long billId, @ModelAttribute("newPayment") NvtPayment payment) {
        try {
            billingService.addPayment(billId, payment);
            return "redirect:/admin/bills/" + billId + "?msg=" + enc("Ghi nhận thanh toán thành công");
        } catch (Exception e) {
            return "redirect:/admin/bills/" + billId + "?err=" + enc(e.getMessage());
        }
    }

    private String enc(String s) {
        return URLEncoder.encode(s == null ? "" : s, StandardCharsets.UTF_8);
    }
}
