package com.UCH.UAContentHub.Controller;

import com.UCH.UAContentHub.Entity.Enum.Role;
import com.UCH.UAContentHub.Entity.Profile;
import com.UCH.UAContentHub.Entity.User;
import com.UCH.UAContentHub.Service.Interface.ProfileService;
import com.UCH.UAContentHub.bean.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@AllArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final HttpSession session;

    private ProfileService profileService;

    @GetMapping("")
    public String ShowProfilePage(Model model) {

        User currentUser = session.getUser();
        if (!session.isPresent()) {
            return "redirect:/auth/login";}
        if(currentUser!=null && currentUser.getRole()== Role.ADMIN){return "redirect:/adminpanel";}
        if (currentUser.getRole() == Role.CREATOR && currentUser.getProfile() == null) {
            currentUser.setProfile(session.getUser().getProfile());}

        model.addAttribute("user", currentUser);
        return "profile";
    }

    @PostMapping("/update")
    public String UpdateUser(@RequestParam(required = false) String email,
                         @RequestParam(required = false) String login,
                         @RequestParam(required = false) String password,
                         @RequestParam(required = false) String name,
                         @RequestParam(required = false) String description,
                         @RequestParam(required = false) String tiktok,
                         @RequestParam(required = false) String instagram,
                         @RequestParam(required = false) String twitch,
                         @RequestParam(required = false) String youtube,
                         @RequestParam(required = false) MultipartFile avatar,
                         RedirectAttributes redirectAttributes){

        User currentUser = session.getUser();
        if (!session.isPresent()) {
            return "redirect:/auth/login";}

        if (email != null && !email.trim().isEmpty()) currentUser.setEmail(email);
        if (login != null && !login.trim().isEmpty()) currentUser.setLogin(login);
        if (password != null && !password.trim().isEmpty()) currentUser.setPassword(password);
        if (name != null && !name.trim().isEmpty()) currentUser.setName(name);

        if (currentUser.getRole() == Role.CREATOR && currentUser.getProfile() != null) {
            Profile currentProfile = currentUser.getProfile();

            if (description != null && !description.trim().isEmpty()) {currentProfile.setDescription(description);}
            if (tiktok != null && !tiktok.trim().isEmpty()) {currentProfile.setTiktok(tiktok);}
            if (instagram != null && !instagram.trim().isEmpty()) {currentProfile.setInstagram(instagram);}
            if (twitch != null && !twitch.trim().isEmpty()) {currentProfile.setTwitch(twitch);}
            if (youtube != null && !youtube.trim().isEmpty()) {currentProfile.setYoutube(youtube);}
            if (avatar != null && !avatar.isEmpty()) {
                try {
                    String avatarUrl = profileService.uploadAvatar(currentUser, avatar);
                    currentProfile.setAvatarURL(avatarUrl);
                } catch (RuntimeException e) {
                    redirectAttributes.addFlashAttribute("error", e.getMessage());
                    redirectAttributes.addFlashAttribute("user", currentUser);
                    return "redirect:/profile";
                }
            }
        }
        currentUser.setRegistrationDate(LocalDateTime.now());//оновлення дати реєстрації

        try {
            profileService.updateU(currentUser);

            if (currentUser.getRole() == Role.CREATOR && currentUser.getProfile() != null) {
                profileService.updateP(currentUser.getProfile());}
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("user", currentUser);
            return "redirect:/profile";
        }

        redirectAttributes.addFlashAttribute("user", currentUser);
        redirectAttributes.addFlashAttribute("message", "Дані успішно оновлено!");
        return "redirect:/profile";
    }
}
