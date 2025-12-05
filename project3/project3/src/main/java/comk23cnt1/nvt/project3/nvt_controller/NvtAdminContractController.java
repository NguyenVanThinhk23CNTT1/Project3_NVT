package comk23cnt1.nvt.project3.nvt_controller;

import comk23cnt1.nvt.project3.nvt_entity.NvtContract;
import comk23cnt1.nvt.project3.nvt_entity.NvtContractMember;
import comk23cnt1.nvt.project3.nvt_service.NvtContractService;
import comk23cnt1.nvt.project3.nvt_service.NvtRoomService;
import comk23cnt1.nvt.project3.nvt_service.NvtTenantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/admin/contracts")
public class NvtAdminContractController {

    private final NvtContractService contractService;
    private final NvtRoomService roomService;
    private final NvtTenantService tenantService;

    public NvtAdminContractController(NvtContractService contractService,
                                      NvtRoomService roomService,
                                      NvtTenantService tenantService) {
        this.contractService = contractService;
        this.roomService = roomService;
        this.tenantService = tenantService;
    }

    @GetMapping
    public String list(Model model,
                       @RequestParam(value="msg", required=false) String msg,
                       @RequestParam(value="err", required=false) String err) {
        model.addAttribute("contracts", contractService.findAll());
        model.addAttribute("newContract", new NvtContract());
        model.addAttribute("rooms", roomService.findAll());
        model.addAttribute("tenants", tenantService.findAll());
        model.addAttribute("statuses", NvtContract.ContractStatus.values());
        model.addAttribute("msg", msg);
        model.addAttribute("err", err);
        return "admin/contracts";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("newContract") NvtContract c) {
        try {
            contractService.create(c);
            return "redirect:/admin/contracts?msg=" + enc("Tạo hợp đồng thành công");
        } catch (Exception e) {
            return "redirect:/admin/contracts?err=" + enc(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model,
                         @RequestParam(value="msg", required=false) String msg,
                         @RequestParam(value="err", required=false) String err) {
        model.addAttribute("contract", contractService.findById(id));
        model.addAttribute("members", contractService.members(id));
        model.addAttribute("tenants", tenantService.findAll());
        model.addAttribute("newMember", new NvtContractMember());
        model.addAttribute("msg", msg);
        model.addAttribute("err", err);
        return "admin/contract-detail";
    }

    @PostMapping("/{id}/add-member")
    public String addMember(@PathVariable Long id, @ModelAttribute("newMember") NvtContractMember m) {
        try {
            contractService.addMember(id, m);
            return "redirect:/admin/contracts/" + id + "?msg=" + enc("Thêm người ở ghép thành công");
        } catch (Exception e) {
            return "redirect:/admin/contracts/" + id + "?err=" + enc(e.getMessage());
        }
    }

    @PostMapping("/{id}/remove-member/{tenantId}")
    public String removeMember(@PathVariable Long id, @PathVariable Long tenantId) {
        try {
            contractService.removeMember(id, tenantId);
            return "redirect:/admin/contracts/" + id + "?msg=" + enc("Cho rời phòng thành công");
        } catch (Exception e) {
            return "redirect:/admin/contracts/" + id + "?err=" + enc(e.getMessage());
        }
    }

    @PostMapping("/{id}/end")
    public String endContract(@PathVariable Long id) {
        try {
            contractService.endContract(id);
            return "redirect:/admin/contracts?msg=" + enc("Kết thúc hợp đồng thành công");
        } catch (Exception e) {
            return "redirect:/admin/contracts?err=" + enc(e.getMessage());
        }
    }

    private String enc(String s) {
        return URLEncoder.encode(s == null ? "" : s, StandardCharsets.UTF_8);
    }
}
