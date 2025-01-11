package com.UCH.UAContentHub.Controller;

import com.UCH.UAContentHub.Entity.Enum.Role;
import com.UCH.UAContentHub.Entity.User;
import com.UCH.UAContentHub.bean.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/")
public class MainController {
    @Autowired
    private final  HttpSession session;


    @GetMapping("")
    public String mainPage(Model model) {
        User user = session.getUser();
        model.addAttribute("user", user);
        return "main";

    }
    @GetMapping("/profile")
    public String profilePage(Model model) {

        User currentUser = session.getUser();
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        if (currentUser.getRole() == Role.CREATOR && currentUser.getProfile() == null) {
            currentUser.setProfile(session.getUser().getProfile());
        }

        model.addAttribute("user", currentUser);
        return "profile";
    }

}
