package com.UCH.UAContentHub.Controller;

import com.UCH.UAContentHub.Entity.Complaint;
import com.UCH.UAContentHub.Entity.Enum.ComplaintStatus;
import com.UCH.UAContentHub.Entity.Enum.CreatorProfileStatus;
import com.UCH.UAContentHub.Entity.Enum.ReviewStatus;
import com.UCH.UAContentHub.Entity.Review;
import com.UCH.UAContentHub.Entity.User;
import com.UCH.UAContentHub.Service.Interface.AdminService;
import com.UCH.UAContentHub.bean.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@AllArgsConstructor
@RequestMapping("/adminpanel")
public class AdminController {
    private final HttpSession session;
    private final AdminService adminService;

    @GetMapping("")
    public String ShowMainPage(Model model) {
        if(!session.isPresent()){return "redirect:/auth/login";}
        User user = session.getUser();
        model.addAttribute("user", user);
        return "adminpanel";
    }
    @GetMapping("/Allcreators")
    public String ShowPendingCreatorsPage(Model model)
    {
        if(!session.isPresent()){return "redirect:/auth/login";}
        List<User>  pendingCreators = adminService.getPendingCreators();
        model.addAttribute("pendingCreators", pendingCreators);
        return "PendingCreators";
    }

    @PostMapping("/deleteUnconfirmedCreators")
    public String DeleteAllUnconfirmedCreators(RedirectAttributes redirectAttributes) {
        adminService.deleteUnconfirmedCreators();
        redirectAttributes.addFlashAttribute("message",
                "Всі непідтверджені автори були успішно видалені.");
        return "redirect:/adminpanel/Allcreators";
    }

    @GetMapping("/Allcreators/creator/{id}")
    public String ShowConfirmCreatorPage(@PathVariable int id,Model model){
        if(!session.isPresent()){return "redirect:/auth/login";}
        User creatorData = adminService.getCreatorById(id);
        model.addAttribute("creator",creatorData);

        return "ConfirmCreator";
    }
    @PostMapping("/changeCreatorStatus/{id}")
    public String changeCreatorStatus(
            @PathVariable int id,
            @RequestParam("status") CreatorProfileStatus status,
            RedirectAttributes redirectAttributes
    ) {
        adminService.changeCreatorStatus(id, status);
        String message = switch (status) {
            case CONFIRMED -> "Автор підтверджений.";
            case UNCONFIRMED -> "Автор відхилений.";
            default -> "Статус автора оновлено.";
        };
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/adminpanel/Allcreators";
    }
    @PostMapping("/deleteCreator/{id}")
    public String DeleteCreator(@PathVariable int id, RedirectAttributes redirectAttributes){
        adminService.deleteCreator(id);
        redirectAttributes.addFlashAttribute("message", "Автор видалений.");
        return "redirect:/adminpanel/Allcreators";
    }

    @GetMapping("/AllReviews")
    public String  ShowPendingReviewsPage(Model model)
    {
        if(!session.isPresent()){return "redirect:/auth/login";}
        List<Review> reviews = adminService.getPendingReviews();
        model.addAttribute("pendingReviews",reviews);
        return "PendingReviews";
    }

    @PostMapping("/deleteNotApprovedReviews")
    public String DeleteAllNotApprovedReviews(RedirectAttributes redirectAttributes) {
        adminService.deleteOldNotApprovedReviews();
        redirectAttributes.addFlashAttribute("message",
                "Всі непідтверджені рецензії були успішно видалені.");
        return "redirect:/adminpanel/AllReviews";
    }

    @GetMapping("/AllReviews/review/{id}")
    public String ShowConfirmReviewPage(@PathVariable int id,Model model){
        if(!session.isPresent()){return "redirect:/auth/login";}
        Review review = adminService.getReviewById(id);
        model.addAttribute("review",review);
        return "ConfirmReview";
    }

    @PostMapping("/changeReviewStatus/{id}")
    public String ChangeReviewStatus(
            @PathVariable int id,
            @RequestParam("status") ReviewStatus status,
            RedirectAttributes redirectAttributes
    ) {
        adminService.changeReviewStatus(id, status);
        String message = switch (status) {
            case APPROVED -> "Рецензія підтверджена.";
            case NOT_APPROVED -> "Рецензія непідтверджена.";
            default -> "Статус рецензії оновлено.";
        };
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/adminpanel/AllReviews";
    }

    @GetMapping("/AllComplaints")
    public String  ShowPendingComplaintsPage(Model model)
    {
        if(!session.isPresent()){return "redirect:/auth/login";}
        List<Complaint> complaints = adminService.getPendingComplaints();
        model.addAttribute("pendingComplaints",complaints);
        return "PendingComplaints";
    }

    @GetMapping("/AllComplaints/complaint/{id}")
    public  String ShowConfirmComplaintPage(@PathVariable int id, Model model)
    {
        if(!session.isPresent()){return "redirect:/auth/login";}
        Complaint complaint = adminService.getComplaintById(id);
        model.addAttribute("complaint",complaint);
        return "ConfirmComplaint";
    }

    @PostMapping("changeComplaintStatus/{id}")
    public  String ChangeComplaintStatus(@PathVariable int id, @RequestParam("status")ComplaintStatus status,
                                         RedirectAttributes redirectAttributes)
    {
        adminService.changeComplaintStatus(id, status);
        String message = switch (status) {
            case  RESOLVED-> "Скарга вирішена.";
            case  DISMISSED-> "Скарга відхилена.";
            default -> "Статус рецензії оновлено.";
        };
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/adminpanel/AllComplaints";}

}
