package com.UCH.UAContentHub.Controller;

import com.UCH.UAContentHub.Entity.Enum.CreatorProfileStatus;
import com.UCH.UAContentHub.Entity.Profile;
import com.UCH.UAContentHub.Entity.User;
import com.UCH.UAContentHub.Service.Interface.ContentService;
import com.UCH.UAContentHub.Service.Interface.ProfileService;
import com.UCH.UAContentHub.bean.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/creator")
public class CreatorProfileController {

    private final HttpSession session;

    private final ProfileService profileService;
    private final ContentService contentService;

    @GetMapping("/{id}")
    public String creatorProfile(@PathVariable int id, Model model) {
        if (!session.isPresent()) {
            return "redirect:/auth/login";
        }

        Profile creatorProfile = contentService.getProfileById(id);
        if (creatorProfile == null || creatorProfile.getStatus() != CreatorProfileStatus.CONFIRMED) {
            return "redirect:/";
        }

        User currentUser = session.getUser();
        boolean isSubscribed = profileService.isSubscribed(creatorProfile, currentUser);

        model.addAttribute("profile", creatorProfile);
        model.addAttribute("user", currentUser);
        model.addAttribute("isSubscribed", isSubscribed);
        return "creatorProfile";
    }
    @GetMapping("/{id}/subscribe")
    public String subscribe(@PathVariable int id) {
        if (!session.isPresent()) {
            return "redirect:/auth/login";
        }

        Profile creatorProfile = profileService.getProfileByID(id);
        if (creatorProfile == null || creatorProfile.getStatus() != CreatorProfileStatus.CONFIRMED) {
            return "redirect:/";
        }

        User currentUser = session.getUser();
        try {
            profileService.subscribeCreator(creatorProfile, currentUser);
        } catch (IllegalArgumentException e) {
            return "redirect:/creator/" + id + "?error=" + e.getMessage();
        }

        return "redirect:/creator/" + id;
    }

    @GetMapping("/{id}/unsubscribe")
    public String unsubscribe(@PathVariable int id) {
        if (!session.isPresent()) {
            return "redirect:/auth/login";
        }

        Profile creatorProfile = profileService.getProfileByID(id);
        if (creatorProfile == null || creatorProfile.getStatus() != CreatorProfileStatus.CONFIRMED) {
            return "redirect:/";
        }

        User currentUser = session.getUser();
        try {
            profileService.unsubscribeCreator(creatorProfile, currentUser);
        } catch (IllegalArgumentException e) {
            return "redirect:/creator/" + id + "?error=" + e.getMessage();
        }

        return "redirect:/creator/" + id;
    }
    @PostMapping("/{userId}/report")
    public String reportProfile(@PathVariable int userId, @RequestParam String reason, Model model) {
        if (!session.isPresent()) {
            return "redirect:/auth/login";
        }

        User currentUser = session.getUser();
        try {
            profileService.reportProfile(userId, currentUser.getId(), reason);
            model.addAttribute("message", "Скаргу успішно подано!");
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }

        Profile creatorProfile = contentService.getProfileById(userId);
        model.addAttribute("profile", creatorProfile);
        model.addAttribute("user", currentUser);
        return "redirect:/creator/" + userId;
    }
}
