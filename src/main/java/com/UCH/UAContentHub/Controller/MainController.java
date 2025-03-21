package com.UCH.UAContentHub.Controller;

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

        List<Profile> confirmedCreators = contentService.getConfirmedCreators();
        List <Tags> tags = contentService.getAllTags();
        model.addAttribute("user", user);

        model.addAttribute("tags", tags);
        if (user != null) {
            List<Profile> recommendedCreators = contentService.getRecommendedCreators(user.getId());
            confirmedCreators = confirmedCreators.stream()
                    .filter(c -> !recommendedCreators.contains(c))
                    .collect(Collectors.toList());
            model.addAttribute("recommendedCreators", recommendedCreators);
        }
        model.addAttribute("creators", confirmedCreators);
        return "main";
    }

    //ТРАБЛ якщо креатора тільки верифікували, то в неї не буде рейтинга. тобто відображатися буде тільки в загальному.
    //тому треба зробити більш детальну фільтрацію
    @GetMapping("/filter")
    public String filterCreators(
            @RequestParam(required = false) Set<String> tags,
            @RequestParam(defaultValue = "1") int minRating,
            @RequestParam(defaultValue = "5") int maxRating,
            Model model) {
        User user = session.getUser();
        if (tags == null) {
            tags = Set.of();
        }
        List<Profile> filteredCreators = contentService.filterByTagsAndRating(tags, minRating, maxRating);
        model.addAttribute("creators", filteredCreators);
        model.addAttribute("tags", contentService.getAllTags());
        model.addAttribute("selectedTags", tags);
        model.addAttribute("user", user);
        if (user != null) {
            List<Profile> recommendedCreators = contentService.getRecommendedCreators(user.getId());

            model.addAttribute("recommendedCreators", recommendedCreators);
        }
        return "main";
    }

}
