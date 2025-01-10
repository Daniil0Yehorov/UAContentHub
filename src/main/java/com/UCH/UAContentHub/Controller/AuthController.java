package com.UCH.UAContentHub.Controller;
import com.UCH.UAContentHub.Entity.Enum.CreatorProfileStatus;
import com.UCH.UAContentHub.Entity.Enum.Role;
import com.UCH.UAContentHub.Entity.Enum.User_Status;
import com.UCH.UAContentHub.Entity.User;
import com.UCH.UAContentHub.Entity.Profile;
import com.UCH.UAContentHub.Service.Interface.AuthService;
import com.UCH.UAContentHub.bean.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    /*@Autowired
    private  final HttpSession session;

    public AuthController(HttpSession session) {
        this.session = session;
    }*/
    //Login зробити
    //мейн контролер розробити на головну сторінку без особливих дій зі сторони користувача


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
                System.out.println(user.getName());
                System.out.println(user.getPassword());
                System.out.println(user.getEmail());
                System.out.println(role);
                System.out.println(description);
                System.out.println(tiktok);
                System.out.println(instagram);
                System.out.println(twitch);
                System.out.println(youtube);
                System.out.println(user.getRole());

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
            } else {
                authService.Register(user);
            }

            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}
