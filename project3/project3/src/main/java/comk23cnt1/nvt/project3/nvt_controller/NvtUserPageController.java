package comk23cnt1.nvt.project3.nvt_controller;

import comk23cnt1.nvt.project3.nvt_entity.NvtBill;
import comk23cnt1.nvt.project3.nvt_entity.NvtBookingRequest;
import comk23cnt1.nvt.project3.nvt_entity.NvtRoom;
import comk23cnt1.nvt.project3.nvt_repository.NvtBillRepository;
import comk23cnt1.nvt.project3.nvt_repository.NvtBookingRequestRepository;
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
    private final NvtBookingRequestRepository bookingRepo;

    public NvtUserPageController(NvtRoomRepository roomRepo,
                                 NvtBillRepository billRepo,
                                 NvtBookingRequestRepository bookingRepo) {
        this.roomRepo = roomRepo;
        this.billRepo = billRepo;
        this.bookingRepo = bookingRepo;
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
        NvtRoom room = roomRepo.findByIdWithHostel(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phòng"));

        model.addAttribute("room", room);
        model.addAttribute("msg", msg);
        model.addAttribute("err", err);
        return "user/room-detail";
    }

    // ✅ Lưu booking request vào DB để admin duyệt
    @PostMapping("/rooms/{id}/book")
    public String book(@PathVariable Long id,
                       @RequestParam String fullName,
                       @RequestParam String phone,
                       @RequestParam(value="note", required=false) String note) {
        try {
            if (fullName == null || fullName.trim().isBlank())
                return "redirect:/rooms/" + id + "?err=" + enc("Họ tên không được rỗng");
            if (phone == null || phone.trim().isBlank())
                return "redirect:/rooms/" + id + "?err=" + enc("SĐT không được rỗng");

            // check room tồn tại
            NvtRoom room = roomRepo.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phòng"));

            // chỉ cho gửi khi phòng đang EMPTY
            if (room.getStatus() != null && room.getStatus() != NvtRoom.RoomStatus.EMPTY) {
                return "redirect:/rooms/" + id + "?err=" + enc("Phòng này hiện không còn trống");
            }

            NvtBookingRequest req = new NvtBookingRequest();
            req.setRoomId(id);
            req.setFullName(fullName.trim());
            req.setPhone(phone.trim());
            req.setNote(note == null || note.trim().isBlank() ? null : note.trim());

            // set status đúng enum (hoặc bỏ dòng này nếu entity @PrePersist set NEW)
            req.setStatus(NvtBookingRequest.Status.NEW);

            bookingRepo.save(req);

            return "redirect:/rooms/" + id + "?msg=" + enc("Đã gửi yêu cầu thuê. Admin sẽ duyệt và liên hệ sớm.");
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
