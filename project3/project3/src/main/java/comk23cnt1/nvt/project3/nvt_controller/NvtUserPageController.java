package comk23cnt1.nvt.project3.nvt_controller;

import comk23cnt1.nvt.project3.nvt_entity.NvtBill;
import comk23cnt1.nvt.project3.nvt_entity.NvtRoom;
import comk23cnt1.nvt.project3.nvt_repository.NvtBillRepository;
import comk23cnt1.nvt.project3.nvt_repository.NvtRoomRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
public class NvtUserPageController {

    private final NvtRoomRepository roomRepo;
    private final NvtBillRepository billRepo;

    public NvtUserPageController(NvtRoomRepository roomRepo, NvtBillRepository billRepo) {
        this.roomRepo = roomRepo;
        this.billRepo = billRepo;
    }

    // Trang chủ: list phòng trống
    @GetMapping("/")
    public String home(Model model,
                       @RequestParam(value="msg", required=false) String msg,
                       @RequestParam(value="err", required=false) String err) {
        List<NvtRoom> emptyRooms = roomRepo.findByStatusOrderByFloorAscRoomCodeAsc(NvtRoom.RoomStatus.EMPTY);
        model.addAttribute("rooms", emptyRooms);
        model.addAttribute("msg", msg);
        model.addAttribute("err", err);
        return "user/home";
    }

    // Chi tiết phòng
    @GetMapping("/rooms/{id}")
    public String roomDetail(@PathVariable Long id, Model model,
                             @RequestParam(value="msg", required=false) String msg,
                             @RequestParam(value="err", required=false) String err) {
        NvtRoom room = roomRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phòng"));
        model.addAttribute("room", room);
        model.addAttribute("msg", msg);
        model.addAttribute("err", err);
        return "user/room-detail";
    }

    // Đặt thuê (tạm thời: chỉ hiển thị thông báo, sau login sẽ tạo booking/request)
    @PostMapping("/rooms/{id}/book")
    public String book(@PathVariable Long id,
                       @RequestParam String fullName,
                       @RequestParam String phone) {
        try {
            if (fullName == null || fullName.trim().isBlank())
                return "redirect:/rooms/" + id + "?err=" + enc("Họ tên không được rỗng");
            if (phone == null || phone.trim().isBlank())
                return "redirect:/rooms/" + id + "?err=" + enc("SĐT không được rỗng");

            // TODO (sau): lưu vào bảng booking_requests
            return "redirect:/rooms/" + id + "?msg=" + enc("Đã gửi yêu cầu đặt thuê. Chủ trọ sẽ liên hệ.");
        } catch (Exception e) {
            return "redirect:/rooms/" + id + "?err=" + enc(e.getMessage());
        }
    }

    // Tra cứu hóa đơn theo phòng (public)
    @GetMapping("/bills")
    public String billLookup(Model model,
                             @RequestParam(value="roomId", required=false) Long roomId,
                             @RequestParam(value="msg", required=false) String msg,
                             @RequestParam(value="err", required=false) String err) {
        model.addAttribute("rooms", roomRepo.findAll());
        model.addAttribute("roomId", roomId);
        model.addAttribute("msg", msg);
        model.addAttribute("err", err);

        if (roomId != null) {
            List<NvtBill> bills = billRepo.findByRoomIdOrderByBillYearDescBillMonthDesc(roomId);
            model.addAttribute("bills", bills);
        } else {
            model.addAttribute("bills", java.util.Collections.emptyList());
        }
        return "user/bill-lookup";
    }

    private String enc(String s) {
        return URLEncoder.encode(s == null ? "" : s, StandardCharsets.UTF_8);
    }
}
