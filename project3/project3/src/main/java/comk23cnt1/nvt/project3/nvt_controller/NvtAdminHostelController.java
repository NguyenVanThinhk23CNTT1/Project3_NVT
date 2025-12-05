package comk23cnt1.nvt.project3.nvt_controller;

import comk23cnt1.nvt.project3.nvt_entity.NvtHostel;
import comk23cnt1.nvt.project3.nvt_service.NvtHostelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/admin/hostels")
public class NvtAdminHostelController {

    private final NvtHostelService hostelService;

    public NvtAdminHostelController(NvtHostelService hostelService) {
        this.hostelService = hostelService;
    }

    @GetMapping
    public String list(Model model,
                       @RequestParam(value="msg", required=false) String msg,
                       @RequestParam(value="err", required=false) String err) {
        model.addAttribute("hostels", hostelService.findAll());
        model.addAttribute("newHostel", new NvtHostel());
        model.addAttribute("msg", msg);
        model.addAttribute("err", err);
        return "admin/hostels";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("newHostel") NvtHostel h) {
        try {
            hostelService.create(h);
            return "redirect:/admin/hostels?msg=" + enc("Thêm nhà trọ thành công");
        } catch (Exception e) {
            return "redirect:/admin/hostels?err=" + enc(e.getMessage());
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        try {
            hostelService.delete(id);
            return "redirect:/admin/hostels?msg=" + enc("Xóa nhà trọ thành công");
        } catch (Exception e) {
            return "redirect:/admin/hostels?err=" + enc(e.getMessage());
        }
    }

    private String enc(String s) {
        return URLEncoder.encode(s == null ? "" : s, StandardCharsets.UTF_8);
    }
}
