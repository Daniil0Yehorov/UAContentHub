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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@AllArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final HttpSession session;

    private ProfileService profileService;

    private static String UPLOADED_FOLDER = "src/main/resources/static/avatars/";
    @GetMapping("")
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

    @PostMapping("/update")
    public String Update( @RequestParam(required = false) String email,
                          @RequestParam(required = false) String login,
                          @RequestParam(required = false) String password,
                          @RequestParam(required = false) String name,
                          @RequestParam(required = false) String description,
                          @RequestParam(required = false) String tiktok,
                          @RequestParam(required = false) String instagram,
                          @RequestParam(required = false) String twitch,
                          @RequestParam(required = false) String youtube,
                          @RequestParam(required = false) MultipartFile avatar,
                          Model model){

        User currentUser = session.getUser();
        if (!session.isPresent()) {
            return "redirect:/auth/login";
        }

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
                    String originalFilename = avatar.getOriginalFilename();
                    String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));

                    if (extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg") || extension.equalsIgnoreCase(".png")) {
                        Path uploadPath = Paths.get(UPLOADED_FOLDER);

                        if (!Files.exists(uploadPath)) {
                            Files.createDirectories(uploadPath);
                        }

                        String oldAvatarPath = currentProfile.getAvatarURL();
                        if (oldAvatarPath != null) {
                            Path oldFilePath = Paths.get("src/main/resources/static" + oldAvatarPath);
                            if (Files.exists(oldFilePath)) {
                                Files.delete(oldFilePath);
                            }
                        }

                        String avatarFileName = currentUser.getLogin() + extension;
                        Path filePath = uploadPath.resolve(avatarFileName);

                        Files.write(filePath, avatar.getBytes());
                        currentProfile.setAvatarURL("/avatars/" + avatarFileName);
                    } else {
                        model.addAttribute("error", "Невірний формат файлу. Підтримуються лише .jpg, .jpeg, .png");
                        model.addAttribute("user", currentUser);
                        return "profile";
                    }
                } catch (Exception e) {
                    model.addAttribute("error", "Помилка при завантаженні аватара: " + e.getMessage());
                    model.addAttribute("user", currentUser);
                    return "profile";
                }
            }
        }

        try {
            profileService.updateU(currentUser);

            if (currentUser.getRole() == Role.CREATOR && currentUser.getProfile() != null) {
                profileService.updateP(currentUser.getProfile());
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", currentUser);
            return "profile";
        }

        model.addAttribute("user", currentUser);
        model.addAttribute("message", "Дані успішно оновлено!");
        return "profile";
    }
}
