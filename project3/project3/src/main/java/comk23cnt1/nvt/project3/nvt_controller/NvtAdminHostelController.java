package comk23cnt1.nvt.project3.nvt_controller;

import comk23cnt1.nvt.project3.nvt_entity.NvtHostel;
import comk23cnt1.nvt.project3.nvt_service.NvtHostelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/hostels")
public class NvtAdminHostelController {

    private final NvtHostelService hostelService;

    public NvtAdminHostelController(NvtHostelService hostelService) {
        this.hostelService = hostelService;
    }

    @GetMapping
    public String list(Model model,
                       @RequestParam(value = "msg", required = false) String msg,
                       @RequestParam(value = "err", required = false) String err) {
        model.addAttribute("hostels", hostelService.findAll());
        model.addAttribute("newHostel", new NvtHostel());
        model.addAttribute("msg", msg);
        model.addAttribute("err", err);
        return "admin/hostels";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("newHostel") NvtHostel h, RedirectAttributes ra) {
        try {
            hostelService.create(h);
            ra.addFlashAttribute("msg", "Thêm nhà trọ thành công");
        } catch (Exception e) {
            ra.addFlashAttribute("err", e.getMessage());
        }
        return "redirect:/admin/hostels";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model,
                       @RequestParam(value = "err", required = false) String err) {
        model.addAttribute("hostel", hostelService.findById(id));
        model.addAttribute("err", err);
        return "admin/hostel-edit";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("hostel") NvtHostel h, RedirectAttributes ra) {
        try {
            hostelService.update(id, h);
            ra.addFlashAttribute("msg", "Cập nhật nhà trọ thành công");
            return "redirect:/admin/hostels";
        } catch (Exception e) {
            return "redirect:/admin/hostels/edit/" + id + "?err=" + enc(e.getMessage());
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            hostelService.delete(id);
            ra.addFlashAttribute("msg", "Xóa nhà trọ thành công");
        } catch (Exception e) {
            ra.addFlashAttribute("err", e.getMessage());
        }
        return "redirect:/admin/hostels";
    }

    private String enc(String s) {
        try {
            return java.net.URLEncoder.encode(s == null ? "" : s, java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "";
        }
    }
}
