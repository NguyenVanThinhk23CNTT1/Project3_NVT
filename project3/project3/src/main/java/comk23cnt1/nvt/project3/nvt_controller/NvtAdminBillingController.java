package comk23cnt1.nvt.project3.nvt_controller;

import comk23cnt1.nvt.project3.nvt_entity.NvtPayment;
import comk23cnt1.nvt.project3.nvt_entity.NvtContract;
import comk23cnt1.nvt.project3.nvt_entity.NvtRoom;
import comk23cnt1.nvt.project3.nvt_entity.NvtTenant;
import comk23cnt1.nvt.project3.nvt_service.NvtBillingService;
import comk23cnt1.nvt.project3.nvt_service.NvtContractService;
import comk23cnt1.nvt.project3.nvt_service.NvtRoomService;
import comk23cnt1.nvt.project3.nvt_service.NvtTenantService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/bills")
public class NvtAdminBillingController {

    private final NvtBillingService billingService;
    private final NvtContractService contractService;
    private final NvtRoomService roomService;
    private final NvtTenantService tenantService;

    public NvtAdminBillingController(
            NvtBillingService billingService,
            NvtContractService contractService,
            NvtRoomService roomService,
            NvtTenantService tenantService
    ) {
        this.billingService = billingService;
        this.contractService = contractService;
        this.roomService = roomService;
        this.tenantService = tenantService;
    }

    @GetMapping
    public String bills(Model model,
                        @RequestParam(value = "msg", required = false) String msg,
                        @RequestParam(value = "err", required = false) String err) {

        var bills = billingService.findAllBills();
        var contracts = contractService.findAll();

        // Tạo map để hiển thị phòng + đại diện
        Map<Long, String> contractMap = new LinkedHashMap<>();
        Map<Long, String> roomMap = new LinkedHashMap<>();

        for (NvtContract c : contracts) {
            // Lấy phòng theo roomId
            NvtRoom room = null;
            try { room = roomService.findById(c.getRoomId()); } catch (Exception ignored) {}

            // Lấy đại diện theo tenantId
            NvtTenant tenant = null;
            try { tenant = tenantService.findById(c.getTenantId()); } catch (Exception ignored) {}

            String roomLabel = (room == null)
                    ? ("Room#" + c.getRoomId())
                    : room.getRoomCode() + " - " + room.getRoomName();

            String tenantLabel = (tenant == null)
                    ? ("Tenant#" + c.getTenantId())
                    : tenant.getFullName() + " - " + tenant.getPhone();

            String contractLabel =
                    c.getContractCode()
                            + " | " + roomLabel
                            + " | Đại diện: " + tenantLabel;

            contractMap.put(c.getId(), contractLabel);
            roomMap.put(c.getRoomId(), roomLabel);
        }

        model.addAttribute("bills", bills);
        model.addAttribute("contracts", contracts);
        model.addAttribute("contractMap", contractMap);
        model.addAttribute("roomMap", roomMap);
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
                             @RequestParam(value = "msg", required = false) String msg,
                             @RequestParam(value = "err", required = false) String err) {

        model.addAttribute("bill", billingService.findBill(billId));
        model.addAttribute("payments", billingService.payments(billId));
        model.addAttribute("newPayment", new NvtPayment());
        model.addAttribute("msg", msg);
        model.addAttribute("err", err);

        return "admin/bill-detail";
    }

    @PostMapping("/{billId}/pay")
    public String pay(@PathVariable Long billId,
                      @ModelAttribute("newPayment") NvtPayment payment) {
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
