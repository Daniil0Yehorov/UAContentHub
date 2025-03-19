package com.UCH.UAContentHub.Controller;

import com.UCH.UAContentHub.Entity.Enum.CreatorProfileStatus;
import com.UCH.UAContentHub.Entity.Enum.Role;
import com.UCH.UAContentHub.Entity.Post;
import com.UCH.UAContentHub.Entity.User;
import com.UCH.UAContentHub.Service.Interface.PostService;
import com.UCH.UAContentHub.bean.HttpSession;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);//логування задля пошуку коренів проблем

    private final HttpSession session;
    private  final PostService postService;

    private boolean isAuthorConfirmed(User user) {
        return user.getProfile().getStatus() == CreatorProfileStatus.CONFIRMED;
    }

    @GetMapping
    public String ShowPostsPage(@RequestParam(required = false) String filter, Model model) {
        User currentUser = session.getUser();

        if (!session.isPresent()) {
            return "redirect:/auth/login";
        }

        List<Post> posts;
        if (currentUser.getRole() == Role.CREATOR) {
            posts = postService.getPostsByUserSortedByDate(currentUser.getId());
        } else {
            if ("subscribed".equals(filter)) {
                posts = postService.getPostsBySubscribedCreators(currentUser.getId());
            } else {
                posts = postService.getAllPostsByCreators();
            }
        }

        model.addAttribute("posts", posts);
        model.addAttribute("user", currentUser);
        model.addAttribute("postService", postService);
        return "posts";
    }

    @PostMapping("/like/{postId}")
    public String likeThePost(@PathVariable int postId) {
        User currentUser = session.getUser();

        if (session.isPresent()) {
            postService.likeThePost(postId, currentUser.getId());}

        return "redirect:/posts";
    }

    @PostMapping("/unlike/{postId}")
    public String unlikeThePost(@PathVariable int postId) {
        User currentUser = session.getUser();

        if (session.isPresent()) {
            postService.CancelTheLikeForThePost(postId, currentUser.getId());}

        return "redirect:/posts";
    }

    @PostMapping("/report/{postId}")
    public String reportThePost(@PathVariable int postId, @RequestParam String reason,
                             RedirectAttributes redirectAttributes) {
        User currentUser = session.getUser();

        if (!session.isPresent()) {
            return "redirect:/auth/login";}

        try {
            postService.reportPost(postId, currentUser.getId(), reason);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/posts";
    }

    @GetMapping("/add")
    public String ShowAddPostPage() {
        User currentUser = session.getUser();

        if (!session.isPresent() || currentUser.getRole() != Role.CREATOR) {
            return "redirect:/auth/login";}

        return "addPost";
    }

    @GetMapping("/edit/{postId}")
    public String ShowEditPostPage(@PathVariable int postId, Model model) {
       User currentUser = session.getUser();

       if (!session.isPresent() || currentUser.getRole() != Role.CREATOR) {
           return "redirect:/auth/login";}

       Post post = postService.getPostById(postId);
       if (post.getProfile().getUser().getId() != currentUser.getId()) {
           return "redirect:/posts";}

       model.addAttribute("user",currentUser);
       model.addAttribute("post", post);
       return "editPost";
   }

    @PostMapping("/add")
    public String addPost(@RequestParam String content,
                          @RequestParam(required = false) MultipartFile[] images,
                          RedirectAttributes redirectAttributes) {
        User currentUser = session.getUser();
        if (!session.isPresent() || currentUser.getRole() != Role.CREATOR) {
            return "redirect:/auth/login";}

        if (!isAuthorConfirmed(currentUser)) {
            redirectAttributes.addFlashAttribute("error",
                    "Ваші дані не підтверджено. Будь ласка, оновіть правильні дані про себе. Ви не можете створювати пости.");
            return "redirect:/posts/add";
        }

        Post post = new Post();
        post.setContent(content);
        post.setProfile(currentUser.getProfile());
        post.setPublishDate(LocalDateTime.now());

        if (images != null && images.length > 0&& !images[0].isEmpty()) {

            try {
                postService.CreatePostwithImages(post, images);
                return "redirect:/posts";
            } catch (IllegalArgumentException e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
                return "redirect:/posts/add";
            }
        }

        try {
            postService.CreatePost(post);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "addPost";
        }
        return "redirect:/posts";
    }

    @PostMapping("/update/{postId}")
    public String updatePost(@PathVariable int postId, @RequestParam String content,
                             RedirectAttributes redirectAttributes) {

        User currentUser = session.getUser();
        if (!session.isPresent() || currentUser.getRole() != Role.CREATOR) {
            return "redirect:/auth/login";}

        Post post = postService.getPostById(postId);

        if (!isAuthorConfirmed(currentUser)) {
            redirectAttributes.addFlashAttribute("user",currentUser);
            redirectAttributes.addFlashAttribute("post", post);
            redirectAttributes.addFlashAttribute("error",
                    "Ваш профіль не підтверджено. Ви не можете оновлювати раніше створені пости.");
            return "redirect:/posts/edit/" +postId;}

        if (post.getProfile().getUser().getId() == currentUser.getId()) {
            post.setContent(content);
            postService.updatePost(post);}

        return "redirect:/posts";
    }

    @PostMapping("/delete/{postId}")
    public String deletePost(@PathVariable int postId,RedirectAttributes  redirectAttributes) {
        User currentUser = session.getUser();

        if (!session.isPresent() || currentUser.getRole() != Role.CREATOR) {
            return "redirect:/auth/login";}

        Post post = postService.getPostById(postId);

        if (!isAuthorConfirmed(currentUser)) {
            redirectAttributes.addFlashAttribute("error",
                    "Ваш профіль не підтверджено. " +
                            "Ви не можете видаляти раніше створені пости.");
            redirectAttributes.addFlashAttribute("user",currentUser);
            redirectAttributes.addFlashAttribute("post", post);
            return "redirect:/posts/edit/" +postId;}

        if (post.getProfile().getUser().getId() == currentUser.getId()) {
            postService.deletePost(postId);}

        return "redirect:/posts";
    }

}
