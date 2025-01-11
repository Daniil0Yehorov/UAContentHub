package com.UCH.UAContentHub.Controller;
import com.UCH.UAContentHub.Entity.Enum.CreatorProfileStatus;
import com.UCH.UAContentHub.Entity.Enum.Role;
import com.UCH.UAContentHub.Entity.Enum.User_Status;
import com.UCH.UAContentHub.Entity.User;
import com.UCH.UAContentHub.Entity.Profile;
import com.UCH.UAContentHub.Service.Interface.AuthService;
import com.UCH.UAContentHub.bean.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private final HttpSession session;



    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("title", "Реєстрація");
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam("login") String login,
                           @RequestParam("password") String password,
                           @RequestParam("name") String name,
                           @RequestParam("email") String email,
                           @RequestParam("role") String roleStr,
                           @RequestParam(value = "description", required = false) String description,
                           @RequestParam(value = "tiktok", required = false) String tiktok,
                           @RequestParam(value = "instagram", required = false) String instagram,
                           @RequestParam(value = "twitch", required = false) String twitch,
                           @RequestParam(value = "youtube", required = false) String youtube,
                           Model model) {
        try {
            Role role = Role.valueOf(roleStr);

            User user = new User();
            user.setStatus(User_Status.ACTIVE);
            user.setLogin(login);
            user.setPassword(password);
            user.setName(name);
            user.setEmail(email);
            user.setRole(role);

            if (role == Role.CREATOR) {
                Profile profile = new Profile();
                profile.setDescription(description);
                profile.setStatus(CreatorProfileStatus.UNCONFIRMED);
                if (tiktok != null) { profile.setTiktok(tiktok); }
                if (instagram != null) { profile.setInstagram(instagram); }
                if (twitch != null) { profile.setTwitch(twitch); }
                if (youtube != null) { profile.setYoutube(youtube); }

                if (description == null || description.isEmpty()) {
                    model.addAttribute("error", "Опис не може бути порожнім");
                    return "register";
                }

                if (tiktok != null && !tiktok.isEmpty() && !authService.isValidUrl(tiktok)) {
                    model.addAttribute("error", "Невірна URL-адреса TikTok");
                    return "register";
                }
                if (instagram != null && !instagram.isEmpty() && !authService.isValidUrl(instagram)) {
                    model.addAttribute("error", "Невірна URL-адреса Instagram");
                    return "register";
                }
                if (twitch != null && !twitch.isEmpty() && !authService.isValidUrl(twitch)) {
                    model.addAttribute("error", "Невірна URL-адреса Twitch");
                    return "register";
                }
                if (youtube != null && !youtube.isEmpty() && !authService.isValidUrl(youtube)) {
                    model.addAttribute("error", "Невірна URL-адреса YouTube");
                    return "register";
                }

                authService.RegisterCreator(user, profile);
                session.setUser(user);
            } else {
                authService.Register(user);
                session.setUser(user);
            }

            return "redirect:/";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("title", "Вхід");
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("login") String login,
                        @RequestParam("password") String password,
                        Model model) {
        try {
            User user = authService.login(login, password);
            if (user == null) {
                model.addAttribute("error", "Неправильний логін або пароль");
                return "login";
            }
            session.setUser(user);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", "Помилка під час входу: " + e.getMessage());
            return "login";
        }
    }
    @GetMapping("/logout")
    public String logout() {
        session.clear();
        return "redirect:/";
    }
}
