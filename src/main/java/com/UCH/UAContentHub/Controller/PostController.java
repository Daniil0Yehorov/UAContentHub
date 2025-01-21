package com.UCH.UAContentHub.Controller;

import com.UCH.UAContentHub.Entity.Enum.Role;
import com.UCH.UAContentHub.Entity.Post;
import com.UCH.UAContentHub.Entity.User;
import com.UCH.UAContentHub.Service.Interface.PostService;
import com.UCH.UAContentHub.Service.Interface.ProfileService;
import com.UCH.UAContentHub.bean.HttpSession;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);//логування задля пошуку коренів проблем

    private final HttpSession session;
    private  final PostService postService;

    @GetMapping
    public String postsPage(@RequestParam(required = false) String filter, Model model) {
        User currentUser = session.getUser();

        if (!session.isPresent()) {
            return "redirect:/auth/login";
        }

        List<Post> posts;
        if (currentUser.getRole() == Role.CREATOR) {
           // posts = postService.getPostsByUser(currentUser.getId());
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
    public String likePost(@PathVariable int postId) {
        User currentUser = session.getUser();
        if (session.isPresent()) {
            postService.likeThePost(postId, currentUser.getId());
        }
        return "redirect:/posts";
    }

    @PostMapping("/unlike/{postId}")
    public String unlikePost(@PathVariable int postId) {
        User currentUser = session.getUser();
        if (session.isPresent()) {
            postService.CancelTheLikeForThePost(postId, currentUser.getId());
        }
        return "redirect:/posts";
    }

    @PostMapping("/report/{postId}")
    public String reportPost(@PathVariable int postId, @RequestParam String reason) {
        User currentUser = session.getUser();
        if (session.isPresent()) {
            postService.reportPost(postId, currentUser.getId(), reason);
        }
        return "redirect:/posts";
    }

    @GetMapping("/add")
    public String addPostPage() {
        User currentUser = session.getUser();
        if (!session.isPresent() || currentUser.getRole() != Role.CREATOR) {
            return "redirect:/auth/login";
        }
        return "addPost";
    }

   @GetMapping("/edit/{postId}")
   public String editPostPage(@PathVariable int postId, Model model) {
       User currentUser = session.getUser();
       if (!session.isPresent() || currentUser.getRole() != Role.CREATOR) {
           return "redirect:/auth/login";
       }

       Post post = postService.getPostById(postId);
       if (post.getProfile().getUser().getId() != currentUser.getId()) {
           return "redirect:/posts";
       }

       model.addAttribute("post", post);
       return "editPost";
   }

    @PostMapping("/add")
    public String addPost(@RequestParam String content) {
        User currentUser = session.getUser();
        if (!session.isPresent() || currentUser.getRole() != Role.CREATOR) {
            return "redirect:/auth/login";
        }

        Post post = new Post();
        post.setContent(content);
        post.setProfile(currentUser.getProfile());
        post.setPublishDate(LocalDateTime.now());
        postService.CreatePost(post);

        return "redirect:/posts";
    }

    @PostMapping("/update/{postId}")
    public String updatePost(@PathVariable int postId, @RequestParam String content) {
        User currentUser = session.getUser();
        if (!session.isPresent() || currentUser.getRole() != Role.CREATOR) {
            return "redirect:/auth/login";
        }

        Post post = postService.getPostById(postId);
        if (post != null && post.getProfile().getUser().getId() == currentUser.getId()) {
            post.setContent(content);
            postService.updatePost(post);
        }

        return "redirect:/posts";
    }

    @PostMapping("/delete/{postId}")
    public String deletePost(@PathVariable int postId) {
        User currentUser = session.getUser();
        if (!session.isPresent() || currentUser.getRole() != Role.CREATOR) {
            return "redirect:/auth/login";
        }

        Post post = postService.getPostById(postId);
        if (post != null && post.getProfile().getUser().getId() == currentUser.getId()) {
            postService.deletePost(postId);
        }

        return "redirect:/posts";
    }

}
