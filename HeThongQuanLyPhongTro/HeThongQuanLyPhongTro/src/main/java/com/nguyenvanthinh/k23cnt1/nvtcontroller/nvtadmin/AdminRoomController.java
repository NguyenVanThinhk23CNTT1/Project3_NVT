package com.nguyenvanthinh.k23cnt1.nvtcontroller.nvtadmin;

import com.nguyenvanthinh.k23cnt1.nvtentity.Room;
import com.nguyenvanthinh.k23cnt1.nvtenums.RoomStatus;
import com.nguyenvanthinh.k23cnt1.nvtservice.HouseService;
import com.nguyenvanthinh.k23cnt1.nvtservice.RoomService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/rooms")
public class AdminRoomController {

    private final RoomService roomService;
    private final HouseService houseService;

    @GetMapping
    public String list(Model model, HttpServletRequest request) {
        model.addAttribute("rooms", roomService.getAll());
        model.addAttribute("currentPath", request.getRequestURI());
        return "admin/room/room-list";
    }

    @GetMapping("/add")
    public String add(Model model, HttpServletRequest request) {
        model.addAttribute("room", new Room());
        model.addAttribute("houses", houseService.getAll());
        model.addAttribute("statuses", RoomStatus.values());
        model.addAttribute("currentPath", request.getRequestURI());
        return "admin/room/room-form";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model, HttpServletRequest request) {
        model.addAttribute("room", roomService.getById(id));
        model.addAttribute("houses", houseService.getAll());
        model.addAttribute("statuses", RoomStatus.values());
        model.addAttribute("currentPath", request.getRequestURI());
        return "admin/room/room-form";
    }

    @PostMapping("/save")
    public String save(Room room) {
        roomService.save(room);
        return "redirect:/admin/rooms";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        roomService.delete(id);
        return "redirect:/admin/rooms";
    }
}
