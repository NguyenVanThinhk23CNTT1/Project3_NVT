package comk23cnt1.nvt.project3.nvt_controller;

import comk23cnt1.nvt.project3.nvt_entity.NvtMeterReading;
import comk23cnt1.nvt.project3.nvt_service.NvtMeterReadingService;
import comk23cnt1.nvt.project3.nvt_service.NvtRoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/admin/meters")
public class NvtAdminMeterController {

    private final NvtMeterReadingService meterService;
    private final NvtRoomService roomService;

    public NvtAdminMeterController(NvtMeterReadingService meterService, NvtRoomService roomService) {
        this.meterService = meterService;
        this.roomService = roomService;
    }

    @GetMapping
    public String page(Model model,
                       @RequestParam(value="roomId", required=false) Long roomId,
                       @RequestParam(value="msg", required=false) String msg,
                       @RequestParam(value="err", required=false) String err) {

        model.addAttribute("rooms", roomService.findAll());
        model.addAttribute("newMeter", new NvtMeterReading());
        model.addAttribute("msg", msg);
        model.addAttribute("err", err);
        model.addAttribute("roomId", roomId);

        if (roomId != null) model.addAttribute("meters", meterService.findByRoom(roomId));
        else model.addAttribute("meters", meterService.findAll());

        return "admin/meters";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("newMeter") NvtMeterReading r) {
        try {
            NvtMeterReading saved = meterService.create(r);
            return "redirect:/admin/meters?roomId=" + saved.getRoomId() + "&msg=" + enc("Lưu chỉ số thành công");
        } catch (Exception e) {
            Long rid = r.getRoomId();
            String back = (rid == null) ? "/admin/meters" : ("/admin/meters?roomId=" + rid);
            return "redirect:" + back + "&err=" + enc(e.getMessage());
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, @RequestParam(value="roomId", required=false) Long roomId) {
        try {
            meterService.delete(id);
            String back = (roomId == null) ? "/admin/meters" : ("/admin/meters?roomId=" + roomId);
            return "redirect:" + back + "&msg=" + enc("Xóa thành công");
        } catch (Exception e) {
            String back = (roomId == null) ? "/admin/meters" : ("/admin/meters?roomId=" + roomId);
            return "redirect:" + back + "&err=" + enc(e.getMessage());
        }
    }

    private String enc(String s) {
        return URLEncoder.encode(s == null ? "" : s, StandardCharsets.UTF_8);
    }
}
