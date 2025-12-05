package comk23cnt1.nvt.project3.nvt_controller;

import comk23cnt1.nvt.project3.nvt_entity.NvtRoom;
import comk23cnt1.nvt.project3.nvt_service.NvtHostelService;
import comk23cnt1.nvt.project3.nvt_service.NvtRoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/rooms")
public class NvtAdminRoomController {

    private final NvtRoomService roomService;
    private final NvtHostelService hostelService;

    public NvtAdminRoomController(NvtRoomService roomService, NvtHostelService hostelService) {
        this.roomService = roomService;
        this.hostelService = hostelService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("rooms", roomService.findAll());
        model.addAttribute("newRoom", new NvtRoom());
        model.addAttribute("statuses", NvtRoom.RoomStatus.values());
        model.addAttribute("hostels", hostelService.findAll()); // thêm nhà trọ
        return "admin/rooms";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("newRoom") NvtRoom newRoom,
                         @RequestParam(name = "imageFile", required = false) MultipartFile imageFile,
                         RedirectAttributes ra) {
        try {
            roomService.create(newRoom, imageFile); // service mới: nhận file
            ra.addFlashAttribute("msg", "Thêm phòng thành công");
            return "redirect:/admin/rooms";
        } catch (Exception e) {
            ra.addFlashAttribute("err", e.getMessage());
            return "redirect:/admin/rooms";
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model,
                       @ModelAttribute("err") String err,
                       @ModelAttribute("msg") String msg) {
        model.addAttribute("room", roomService.findById(id));
        model.addAttribute("statuses", NvtRoom.RoomStatus.values());
        model.addAttribute("hostels", hostelService.findAll());
        model.addAttribute("err", err);
        model.addAttribute("msg", msg);
        return "admin/room-edit";
    }


    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute NvtRoom room,
                         @RequestParam(name = "imageFile", required = false) MultipartFile imageFile,
                         RedirectAttributes ra) {
        try {
            roomService.update(id, room, imageFile); // service mới: nhận file
            ra.addFlashAttribute("msg", "Cập nhật thành công");
            return "redirect:/admin/rooms";
        } catch (Exception e) {
            ra.addFlashAttribute("err", e.getMessage());
            return "redirect:/admin/rooms/edit/" + id;
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            roomService.delete(id);
            ra.addFlashAttribute("msg", "Xóa thành công");
            return "redirect:/admin/rooms";
        } catch (Exception e) {
            ra.addFlashAttribute("err", e.getMessage());
            return "redirect:/admin/rooms";
        }
    }
}
