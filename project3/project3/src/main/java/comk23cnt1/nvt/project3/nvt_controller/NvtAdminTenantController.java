package comk23cnt1.nvt.project3.nvt_controller;

import comk23cnt1.nvt.project3.nvt_entity.NvtTenant;
import comk23cnt1.nvt.project3.nvt_service.NvtTenantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/admin/tenants")
public class NvtAdminTenantController {

    private final NvtTenantService tenantService;

    public NvtAdminTenantController(NvtTenantService tenantService) {
        this.tenantService = tenantService;
    }

    @GetMapping
    public String list(Model model,
                       @RequestParam(value = "msg", required = false) String msg,
                       @RequestParam(value = "err", required = false) String err) {
        model.addAttribute("tenants", tenantService.findAll());
        model.addAttribute("newTenant", new NvtTenant());
        model.addAttribute("statuses", NvtTenant.TenantStatus.values());
        model.addAttribute("msg", msg);
        model.addAttribute("err", err);
        return "admin/tenants";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("newTenant") NvtTenant newTenant) {
        try {
            tenantService.create(newTenant);
            return "redirect:/admin/tenants?msg=" + enc("Thêm khách thuê thành công");
        } catch (Exception e) {
            return "redirect:/admin/tenants?err=" + enc(e.getMessage());
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model,
                       @RequestParam(value = "err", required = false) String err) {
        model.addAttribute("tenant", tenantService.findById(id));
        model.addAttribute("statuses", NvtTenant.TenantStatus.values());
        model.addAttribute("err", err);
        return "admin/tenant-edit";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @ModelAttribute NvtTenant tenant) {
        try {
            tenantService.update(id, tenant);
            return "redirect:/admin/tenants?msg=" + enc("Cập nhật thành công");
        } catch (Exception e) {
            return "redirect:/admin/tenants/edit/" + id + "?err=" + enc(e.getMessage());
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        try {
            tenantService.delete(id);
            return "redirect:/admin/tenants?msg=" + enc("Xóa thành công");
        } catch (Exception e) {
            return "redirect:/admin/tenants?err=" + enc(e.getMessage());
        }
    }

    private String enc(String s) {
        return URLEncoder.encode(s == null ? "" : s, StandardCharsets.UTF_8);
    }
}
