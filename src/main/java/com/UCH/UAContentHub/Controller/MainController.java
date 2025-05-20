package com.UCH.UAContentHub.Controller;

import com.UCH.UAContentHub.Entity.Enum.Role;
import com.UCH.UAContentHub.Entity.Tags;
import com.UCH.UAContentHub.Entity.User;
import com.UCH.UAContentHub.Entity.Profile;
import com.UCH.UAContentHub.Service.Interface.ContentService;
import com.UCH.UAContentHub.bean.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping("/")
public class MainController {

    private final HttpSession session;

    private ContentService contentService;

    @GetMapping("")
    public String ShowMainPage(Model model) {
        User user = session.getUser();
        if(user != null && user.getRole()==Role.ADMIN){return "redirect:/adminpanel";}

        List<Profile> confirmedCreators = contentService.getConfirmedCreators();
        List <Tags> tags = contentService.getAllTags();
        model.addAttribute("user", user);

        model.addAttribute("tags", tags);
        if (user != null && user.getRole()== Role.USER) {
            List<Profile> recommendedCreators = contentService.getRecommendedCreators(user.getId());
            confirmedCreators = confirmedCreators.stream()
                    .filter(c -> !recommendedCreators.contains(c))
                    .collect(Collectors.toList());
            model.addAttribute("recommendedCreators", recommendedCreators);
        }
        model.addAttribute("creators", confirmedCreators);
        return "main";
    }

    @GetMapping("/filter")
    public String filterCreators(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Set<String> tags,
            @RequestParam(defaultValue = "1") int minRating,
            @RequestParam(defaultValue = "5") int maxRating,
            @RequestParam(defaultValue = "0") int minSubscribers,
            @RequestParam(defaultValue = "2147483646") int maxSubscribers,
            Model model) {

        User user = session.getUser();
        if (tags == null) tags = Set.of();

        List<Profile> filtered = contentService.filterCreators(name, tags, minRating, maxRating, minSubscribers, maxSubscribers);

        model.addAttribute("creators", filtered);
        model.addAttribute("tags", contentService.getAllTags());
        model.addAttribute("selectedTags", tags);
        model.addAttribute("user", user);
        model.addAttribute("name", name);
        model.addAttribute("minRating", minRating);
        model.addAttribute("maxRating", maxRating);
        model.addAttribute("minSubscribers", minSubscribers);
        model.addAttribute("maxSubscribers", maxSubscribers);

        if (user != null) {
            model.addAttribute("recommendedCreators", contentService.getRecommendedCreators(user.getId()));
        }

        return "main";
    }

}
