package com.UCH.UAContentHub.Controller;

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

@Controller
@AllArgsConstructor
@RequestMapping("/")
public class MainController {

    private final HttpSession session;

    private ContentService contentService;

    @GetMapping("")
    public String mainPage(Model model) {
        User user = session.getUser();

        List<Profile> confirmedCreators = contentService.getConfirmedCreators();
        model.addAttribute("user", user);
        model.addAttribute("creators", confirmedCreators);

        return "main";
    }

    //мб додати окрему фільтрацію за певними параметрами;
    //ТРАБЛ якщо людину тільки верифікували, то в неї не буде рейтинга
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

}
