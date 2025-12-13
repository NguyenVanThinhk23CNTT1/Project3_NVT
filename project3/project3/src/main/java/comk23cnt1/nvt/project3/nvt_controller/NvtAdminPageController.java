package comk23cnt1.nvt.project3.nvt_controller;

import comk23cnt1.nvt.project3.nvt_entity.NvtBill;
import comk23cnt1.nvt.project3.nvt_entity.NvtFeedback;
import comk23cnt1.nvt.project3.nvt_entity.NvtRoom;
import comk23cnt1.nvt.project3.nvt_repository.NvtBillRepository;
import comk23cnt1.nvt.project3.nvt_repository.NvtFeedbackRepository;
import comk23cnt1.nvt.project3.nvt_repository.NvtRoomRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NvtAdminPageController {

    private final NvtRoomRepository roomRepository;
    private final NvtBillRepository billRepository;
    private final NvtFeedbackRepository feedbackRepository;

    public NvtAdminPageController(NvtRoomRepository roomRepository,
                                  NvtBillRepository billRepository,
                                  NvtFeedbackRepository feedbackRepository) {
        this.roomRepository = roomRepository;
        this.billRepository = billRepository;
        this.feedbackRepository = feedbackRepository;
    }

    @GetMapping("/admin")
    public String adminHome(Model model) {

        long emptyRooms = roomRepository.countByStatus(NvtRoom.RoomStatus.EMPTY);
        long rentingRooms = roomRepository.countByStatus(NvtRoom.RoomStatus.RENTING);

        long unpaidBills = billRepository.countByStatus(NvtBill.BillStatus.UNPAID);
        long overdueBills = billRepository.countByStatus(NvtBill.BillStatus.OVERDUE);

        long newFeedbacks = feedbackRepository.countByStatus(NvtFeedback.FeedbackStatus.NEW);

        model.addAttribute("emptyRooms", emptyRooms);
        model.addAttribute("rentingRooms", rentingRooms);

        model.addAttribute("unpaidBills", unpaidBills);
        model.addAttribute("overdueBills", overdueBills);
        model.addAttribute("unpaidTotal", unpaidBills + overdueBills);

        model.addAttribute("newFeedbacks", newFeedbacks);

        return "admin/index";
    }
}
