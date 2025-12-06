package comk23cnt1.nvt.project3.nvt_controller;

import comk23cnt1.nvt.project3.nvt_entity.NvtContract;
import comk23cnt1.nvt.project3.nvt_entity.NvtContractMember;
import comk23cnt1.nvt.project3.nvt_entity.NvtRoom;
import comk23cnt1.nvt.project3.nvt_entity.NvtTenant;
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

        List<NvtContract> contracts = contractService.findAll();
        List<NvtRoom> rooms = roomService.findAll();
        List<NvtTenant> tenants = tenantService.findAll();

        // roomId -> "P101 - Phòng 101 (hostelId=..)"
        Map<Long, String> roomMap = rooms.stream()
                .filter(r -> r.getId() != null)
                .collect(Collectors.toMap(
                        NvtRoom::getId,
                        this::roomLabel,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));

        // tenantId -> "Nguyễn Văn A - 09xx"
        Map<Long, String> tenantMap = tenants.stream()
                .filter(t -> t.getId() != null)
                .collect(Collectors.toMap(
                        NvtTenant::getId,
                        this::tenantLabel,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));

        model.addAttribute("contracts", contracts);
        model.addAttribute("newContract", new NvtContract());
        model.addAttribute("rooms", rooms);
        model.addAttribute("tenants", tenants);
        model.addAttribute("statuses", NvtContract.ContractStatus.values());

        // ✅ map cho contracts.html
        model.addAttribute("roomMap", roomMap);
        model.addAttribute("tenantMap", tenantMap);

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

        NvtContract contract = contractService.findById(id);
        List<NvtContractMember> members = contractService.members(id);

        List<NvtTenant> tenants = tenantService.findAll();
        List<NvtRoom> rooms = roomService.findAll();

        Map<Long, String> memberTenantMap = tenants.stream()
                .filter(t -> t.getId() != null)
                .collect(Collectors.toMap(
                        NvtTenant::getId,
                        this::tenantLabel,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));

        // roomText cho contract-detail.html
        String roomText = null;
        if (contract.getRoomId() != null) {
            for (NvtRoom r : rooms) {
                if (contract.getRoomId().equals(r.getId())) {
                    roomText = roomLabel(r);
                    break;
                }
            }
        }

        // repTenantText cho contract-detail.html
        String repTenantText = null;
        if (contract.getTenantId() != null) {
            for (NvtTenant t : tenants) {
                if (contract.getTenantId().equals(t.getId())) {
                    repTenantText = tenantLabel(t);
                    break;
                }
            }
        }

        model.addAttribute("contract", contract);
        model.addAttribute("members", members);
        model.addAttribute("tenants", tenants);
        model.addAttribute("newMember", new NvtContractMember());

        // ✅ map cho contract-detail.html
        model.addAttribute("roomText", roomText);
        model.addAttribute("repTenantText", repTenantText);
        model.addAttribute("memberTenantMap", memberTenantMap);

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

    // Nếu bạn chưa làm resume thì để đây (HTML đã có nút).
    // Làm xong service nói mình, hoặc tạm thời comment nút "Tiếp tục" trong html nếu chưa dùng.
    @PostMapping("/{id}/resume")
    public String resumeContract(@PathVariable Long id) {
        try {
            contractService.resumeContract(id); // <-- cần thêm method này vào service
            return "redirect:/admin/contracts?msg=" + enc("Tiếp tục hợp đồng thành công");
        } catch (Exception e) {
            return "redirect:/admin/contracts?err=" + enc(e.getMessage());
        }
    }

    private String roomLabel(NvtRoom r) {
        String code = (r.getRoomCode() == null) ? "" : r.getRoomCode().trim();
        String name = (r.getRoomName() == null) ? "" : r.getRoomName().trim();
        String base = (code.isBlank() && name.isBlank())
                ? ("Room#" + r.getId())
                : (code + (name.isBlank() ? "" : " - " + name));
        if (r.getHostelId() != null) {
            base += " (hostelId=" + r.getHostelId() + ")";
        }
        return base;
    }

    private String tenantLabel(NvtTenant t) {
        String name = (t.getFullName() == null) ? "" : t.getFullName().trim();
        String phone = (t.getPhone() == null) ? "" : t.getPhone().trim();
        if (name.isBlank() && phone.isBlank()) return "Tenant#" + t.getId();
        if (phone.isBlank()) return name;
        if (name.isBlank()) return phone;
        return name + " - " + phone;
    }

    private String enc(String s) {
        return URLEncoder.encode(s == null ? "" : s, StandardCharsets.UTF_8);
    }
}
