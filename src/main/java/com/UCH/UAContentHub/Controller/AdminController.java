package com.UCH.UAContentHub.Controller;

import com.UCH.UAContentHub.Entity.User;
import com.UCH.UAContentHub.Service.Interface.AdminService;
import com.UCH.UAContentHub.bean.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @PostMapping("/deleteUnconfirmed")
    public String deleteAllUnconfirmedCreators(RedirectAttributes redirectAttributes) {
        adminService.deleteUnconfirmedCreators();
        redirectAttributes.addFlashAttribute("message",
                "Всі непідтверджені автори були успішно видалені.");
        return "redirect:/adminpanel/Allcreators";
    }
    /*
    @GetMapping("/Allcreators/creator/{id}")
    public String ShowConfirmCreatorPage(@PathVariable int id,Model model){
        if(!session.isPresent()){return "redirect:/auth/login";}
        User creatorData = adminService.getCreatorById(id);
        model.addAttribute("creator",creatorData);

        return "ConfirmCreatorPage";
    }*/
    /*post підтвердження та ні*/


}
