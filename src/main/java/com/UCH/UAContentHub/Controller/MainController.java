package com.UCH.UAContentHub.Controller;

import com.UCH.UAContentHub.Entity.Enum.CreatorProfileStatus;
import com.UCH.UAContentHub.Entity.Enum.Role;
import com.UCH.UAContentHub.Entity.User;
import com.UCH.UAContentHub.Entity.Profile;
import com.UCH.UAContentHub.Service.Interface.ContentService;
import com.UCH.UAContentHub.bean.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@Controller
@AllArgsConstructor
@RequestMapping("/")
public class MainController {
    @Autowired
    private final  HttpSession session;
    @Autowired
    private ContentService contentService;

    @GetMapping("")
    public String mainPage(Model model) {
        User user = session.getUser();

        List<Profile> confirmedCreators = contentService.getConfirmedCreators();
        model.addAttribute("user", user);
        model.addAttribute("creators", confirmedCreators);

        return "main";
    }


    @GetMapping("/filter")
    public String filterCreators(
            @RequestParam(required = false) Set<String> tags,
            @RequestParam(defaultValue = "1") int minRating,
            @RequestParam(defaultValue = "5") int maxRating,
            Model model) {

        List<Profile> filteredCreators = contentService.filterByTagsAndRating(tags, minRating, maxRating);
        model.addAttribute("creators", filteredCreators);

        return "main";
    }
    @GetMapping("/profile")
    public String profilePage(Model model) {

        User currentUser = session.getUser();
        if (!session.isPresent()) {
            return "redirect:/auth/login";
        }

        if (currentUser.getRole() == Role.CREATOR && currentUser.getProfile() == null) {
            currentUser.setProfile(session.getUser().getProfile());
        }

        model.addAttribute("user", currentUser);
        return "profile";
    }
    @GetMapping("/creator/{id}")
    public String creatorProfile(@PathVariable int id, Model model) {
        if (!session.isPresent()) {
            return "redirect:/auth/login";
        }

        Profile creatorProfile = contentService.getProfileById(id);
        if (creatorProfile == null || creatorProfile.getStatus() != CreatorProfileStatus.CONFIRMED) {
            return "redirect:/";
        }

        model.addAttribute("profile", creatorProfile);
        model.addAttribute("user", session.getUser());
        return "creatorProfile";
    }
}
