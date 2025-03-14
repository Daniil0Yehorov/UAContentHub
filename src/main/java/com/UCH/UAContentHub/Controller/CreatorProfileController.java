package com.UCH.UAContentHub.Controller;

import com.UCH.UAContentHub.Entity.Enum.CreatorProfileStatus;
import com.UCH.UAContentHub.Entity.Enum.ReviewStatus;
import com.UCH.UAContentHub.Entity.Profile;
import com.UCH.UAContentHub.Entity.Review;
import com.UCH.UAContentHub.Entity.User;
import com.UCH.UAContentHub.Service.Interface.ContentService;
import com.UCH.UAContentHub.Service.Interface.ProfileService;
import com.UCH.UAContentHub.Service.Interface.ReviewService;
import com.UCH.UAContentHub.bean.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
@RequestMapping("/creator")
public class CreatorProfileController {

    private final HttpSession session;

    private final ProfileService profileService;
    private final ContentService contentService;
    private final ReviewService reviewService;

    @GetMapping("/{id}")
    public String creatorProfile(@PathVariable int id, Model model) {

        Profile creatorProfile = contentService.getProfileById(id);
        if (creatorProfile == null || creatorProfile.getStatus() != CreatorProfileStatus.CONFIRMED) {
            return "redirect:/";
        }

        User currentUser = session.isPresent() ? session.getUser() : null;
        boolean isSubscribed = currentUser != null && profileService.isSubscribed(creatorProfile, currentUser);
        Review userReview = (currentUser != null) ? reviewService.getReviewByUserAndCreator(currentUser.getId(), creatorProfile.getUser().getId()) : null;
        boolean hasReported = currentUser != null && profileService.hasUserReportedProfile(currentUser.getId(), creatorProfile.getUser().getId());

        model.addAttribute("profile", creatorProfile);
        model.addAttribute("user", currentUser);
        model.addAttribute("isSubscribed", isSubscribed);
        model.addAttribute("userReview", userReview);
        model.addAttribute("hasReported", hasReported);
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
    public String reportProfile(@PathVariable int userId, @RequestParam String reason,
                                RedirectAttributes redirectAttributes) {
        if (!session.isPresent()) {
            return "redirect:/auth/login";
        }

        User currentUser = session.getUser();
        try {
            profileService.reportProfile(userId, currentUser.getId(), reason);
            redirectAttributes.addFlashAttribute("message", "Скаргу успішно подано!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/creator/" + userId;
    }
    @PostMapping("/{creatorId}/review")
    public String addReview(@PathVariable int creatorId,
                            @RequestParam String text,
                            @RequestParam int rating,
                            RedirectAttributes redirectAttributes) {

        if (!session.isPresent()) {
            return "redirect:/auth/login";
        }

        User currentUser = session.getUser();
        Profile creatorProfile = profileService.getProfileByID(creatorId);

        if (creatorProfile == null || creatorProfile.getStatus() != CreatorProfileStatus.CONFIRMED) {
            return "redirect:/";
        }
        Review existingReview = reviewService.getReviewByUserAndCreator(currentUser.getId(), creatorProfile.getUser().getId());
        if (existingReview != null && existingReview.getStatus() != ReviewStatus.NOT_APPROVED) {
            redirectAttributes.addFlashAttribute("error", "Ви вже залишили відгук цьому креатору, але він не має статусу 'NOT_APPROVED'.");
            return "redirect:/creator/" + creatorId;
        }
        try {
            Review review = new Review();
            review.setText(text);
            review.setRating(rating);
            review.setUser(currentUser);
            review.setCreator(creatorProfile.getUser());
            reviewService.createReview(review);
            redirectAttributes.addFlashAttribute("message", "Відгук успішно додано!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/creator/" + creatorId;
    }
    @PostMapping("/{creatorId}/review/delete")
    public String deleteReview(@PathVariable int creatorId, @RequestParam int reviewId,
                               RedirectAttributes redirectAttributes) {
        if (!session.isPresent()) {
            return "redirect:/auth/login";
        }

        try {
            reviewService.deleteReview(reviewId);
            redirectAttributes.addFlashAttribute("message", "Відгук успішно видалено.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/creator/" + creatorId;
    }
    @GetMapping("/{id}/reviews")
    public String viewReviews(@PathVariable int id, Model model) {
        Profile creatorProfile = contentService.getProfileById(id);
        if (creatorProfile == null || creatorProfile.getStatus() != CreatorProfileStatus.CONFIRMED) {
            return "redirect:/";
        }

        model.addAttribute("reviews", reviewService.getReviewsByCreator(creatorProfile.getUser().getId()));
        model.addAttribute("creatorId", id);
        return "creatorReviews";
    }
}
