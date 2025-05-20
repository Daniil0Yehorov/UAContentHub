package com.UCH.UAContentHub.Controller;
import com.UCH.UAContentHub.Entity.*;
import com.UCH.UAContentHub.Entity.Enum.CreatorProfileStatus;
import com.UCH.UAContentHub.Entity.Enum.ReviewStatus;
import com.UCH.UAContentHub.Entity.Enum.Role;
import com.UCH.UAContentHub.Repository.*;
import com.UCH.UAContentHub.Service.Interface.AuthService;
import com.UCH.UAContentHub.bean.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.security.crypto.password.PasswordEncoder;


@Controller
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    private final HttpSession session;

    private final PasswordEncoder passwordEncoder;

    UserRepository userRepository;
    ProfileRepository profileRepository;
    TagsRepository tagsRepository;
    Profile_has_tagsRepository profileHasTagsRepository;
    PostRepository postRepository;
    ReviewRepository reviewRepository;
    SubscriptionRepository subscriptionRepository;

    //додавання даних до бд
    private void initData() {
        List<User> creators = new ArrayList<>();
        List <User> usersWithReviews=new ArrayList<>();
        List<Profile> profiles = new ArrayList<>();
        List<Tags> tags = new ArrayList<>();
        List<Profile_has_tags> profileHasTagsList = new ArrayList<>();
        List<Post> posts = new ArrayList<>();
        List<Review> reviews = new ArrayList<>();
        List<Subscription> subscriptions = new ArrayList<>();

        for (int i = 1; i <= 8; i++) {
            User user = new User();
            user.setName("User" + i);
            user.setLogin("User" + i + "Login");
            user.setPassword(passwordEncoder.encode("Password" + i));
            user.setEmail("User" + i + "@example.com");
            user.setRole(Role.CREATOR);
            user.setRegistrationDate(LocalDateTime.now());

            Profile profile = new Profile();
            profile.setStatus(CreatorProfileStatus.PENDING);//or confirmed
            profile.setTiktok("https://tiktok.com/user" + i);
            profile.setInstagram("https://instagram.com/user" + i);
            profile.setTwitch("https://twitch.tv/user" + i);
            profile.setYoutube("https://youtube.com/user" + i);
            profile.setAvatarURL("/avatars/"+"User" + i + "Login"+".jpg");
            profile.setDescription("Description for User" + i);

            Post newPost=new Post();
            newPost.setTitle("назвачизаголовокпосту");
            newPost.setPublishDate(LocalDateTime.now());
            newPost.setContent("KFKFKFKFKKFKFKFKFKFKFKK"+i);
            newPost.setProfile(profile);

            user.setProfile(profile);
            profile.setUser(user);

            creators.add(user);
            profiles.add(profile);
            posts.add(newPost);
        }
        for (int i = 1; i <= 7; i++) {
            User user = new User();
            user.setName("User" + i +"USER"+ i);
            user.setLogin("User"  + "Login"+ i);
            user.setPassword(passwordEncoder.encode("UserPassword" + i));
            user.setEmail(i+"User" + i + "@gmail.com");
            user.setRole(Role.USER);
            user.setRegistrationDate(LocalDateTime.now());

            usersWithReviews.add(user);
        }
        for (int i = 1; i <= 5; i++) {
            Tags tag = new Tags();
            tag.setName("Tag" + i);
            tags.add(tag);
        }

        for (int i = 0; i < profiles.size(); i++) {
            Profile profile = profiles.get(i);
            for (int j = 0; j < 2; j++) {
                Profile_has_tags profileHasTags = new Profile_has_tags();
                profileHasTags.setProfile(profile);
                profileHasTags.setTags(tags.get((i + j) % tags.size()));
                profileHasTagsList.add(profileHasTags);
            }
        }
        for (User user : usersWithReviews) {
            for (User creator : creators) {
                Review review = new Review();
                review.setUser(user);
                review.setCreator(creator);
                review.setText("Review from " + user.getName() + " to " + creator.getName());
                review.setRating(ThreadLocalRandom.current().nextInt(1, 6));
                review.setReviewDate(LocalDateTime.now());
                review.setStatus(ReviewStatus.PENDING);

                reviews.add(review);

                Subscription subscription = new Subscription();
                subscription.setUser(user);
                subscription.setCreator(creator);
                subscription.setSubscriptionDate(LocalDateTime.now());
                subscriptions.add(subscription);
            }
        }

        userRepository.saveAll(creators);
        userRepository.saveAll(usersWithReviews);
        tagsRepository.saveAll(tags);
        profileHasTagsRepository.saveAll(profileHasTagsList);
        postRepository.saveAll(posts);
        reviewRepository.saveAll(reviews);
        subscriptionRepository.saveAll(subscriptions);
        User ADminuser=new User();
        ADminuser.setName("ADMIIIIINCHIK2332");
        ADminuser.setLogin("Administrator1");
        ADminuser.setPassword(passwordEncoder.encode("AdminPassword"));
        ADminuser.setEmail("Admin1@gmail.com");
        ADminuser.setRole(Role.ADMIN);
        ADminuser.setRegistrationDate(LocalDateTime.now());
        userRepository.save(ADminuser);
    }
    private void  initdataforjmete() {
        List<User> creators = new ArrayList<>();
        List<Profile> profiles = new ArrayList<>();
        List<Post> posts = new ArrayList<>();
        List<Tags> tags = new ArrayList<>();
        List<Profile_has_tags> profileHasTagsList = new ArrayList<>();
        List<User> users = new ArrayList<>();
        List<Subscription> subscriptions = new ArrayList<>();

        // Створення 500 авторів
        for (int i = 1; i <= 500; i++) {
            User creator = new User();
            creator.setName("Creator" + i);
            creator.setLogin("creator_login_" + i);
            creator.setPassword(passwordEncoder.encode("Password" + i));
            creator.setEmail("creator" + i + "@example.com");
            creator.setRole(Role.CREATOR);
            creator.setRegistrationDate(LocalDateTime.now());

            Profile profile = new Profile();
            profile.setStatus(CreatorProfileStatus.CONFIRMED);
            profile.setTiktok("https://tiktok.com/creator" + i);
            profile.setInstagram("https://instagram.com/creator" + i);
            profile.setTwitch("https://twitch.tv/creator" + i);
            profile.setYoutube("https://youtube.com/creator" + i);
            profile.setAvatarURL("/avatars/User" + i + "Login.jpg");
            profile.setDescription("Description for Creator " + i);

            Post post = new Post();
            post.setTitle("Пост " + i);
            post.setPublishDate(LocalDateTime.now());
            post.setContent("Content for post " + i);
            post.setProfile(profile);

            creator.setProfile(profile);
            profile.setUser(creator);

            creators.add(creator);
            profiles.add(profile);
            posts.add(post);
        }

        // Створення 500 звичайних користувачів
        for (int i = 1; i <= 500; i++) {
            User user = new User();
            user.setName("User" + i);
            user.setLogin("user_login_" + i);
            user.setPassword(passwordEncoder.encode("UserPassword" + i));
            user.setEmail("user" + i + "@gmail.com");
            user.setRole(Role.USER);
            user.setRegistrationDate(LocalDateTime.now());
            users.add(user);
        }

        // Створення 10 тегів
        for (int i = 1; i <= 100; i++) {
            Tags tag = new Tags();
            tag.setName("Tag" + i);
            tags.add(tag);
        }

        // Прив'язка тегів до профілів
        for (Profile profile : profiles) {
            for (int j = 0; j < 2; j++) {
                Profile_has_tags link = new Profile_has_tags();
                link.setProfile(profile);
                link.setTags(tags.get(ThreadLocalRandom.current().nextInt(tags.size())));
                profileHasTagsList.add(link);
            }
        }

        // Рандомні підписки
        for (User user : users) {
            Set<User> subscribed = new HashSet<>();
            int subscriptionsCount = ThreadLocalRandom.current().nextInt(10, 21); // від 10 до 20 підписок

            while (subscribed.size() < subscriptionsCount) {
                User randomCreator = creators.get(ThreadLocalRandom.current().nextInt(creators.size()));
                if (!subscribed.contains(randomCreator)) {
                    subscribed.add(randomCreator);
                    Subscription subscription = new Subscription();
                    subscription.setUser(user);
                    subscription.setCreator(randomCreator);
                    subscription.setSubscriptionDate(LocalDateTime.now());
                    subscriptions.add(subscription);
                }
            }
        }

        // Збереження
        userRepository.saveAll(creators);
        userRepository.saveAll(users);
        tagsRepository.saveAll(tags);
        profileHasTagsRepository.saveAll(profileHasTagsList);
        postRepository.saveAll(posts);
        subscriptionRepository.saveAll(subscriptions);

        // Додати адміністратора
        User admin = new User();
        admin.setName("Administrator");
        admin.setLogin("admin_login");
        admin.setPassword(passwordEncoder.encode("AdminPassword"));
        admin.setEmail("admin@example.com");
        admin.setRole(Role.ADMIN);
        admin.setRegistrationDate(LocalDateTime.now());
        userRepository.save(admin);
    }
    @GetMapping("/register")
    public String showRegistrationPage() {
        //initData();
        // initdataforjmete();
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam("login") String login,
                           @RequestParam("password") String password,
                           @RequestParam("name") String name,
                           @RequestParam("email") String email,
                           @RequestParam("role") String roleStr,
                           @RequestParam(value = "description", required = false) String description,
                           @RequestParam(value = "tiktok", required = false) String tiktok,
                           @RequestParam(value = "instagram", required = false) String instagram,
                           @RequestParam(value = "twitch", required = false) String twitch,
                           @RequestParam(value = "youtube", required = false) String youtube,
                           @RequestParam(value = "avatar", required = false) MultipartFile avatar,
                           RedirectAttributes redirectAttributes) {
        try {
            Role role = Role.valueOf(roleStr);
            User user = new User();
            user.setLogin(login);
            user.setPassword(password);
            user.setName(name);
            user.setEmail(email);
            user.setRole(role);

            if (role == Role.CREATOR) {

                Profile profile = new Profile();
                profile.setDescription(description);
                profile.setStatus(CreatorProfileStatus.PENDING);
                if (tiktok == null || tiktok.trim().isEmpty()) {
                    profile.setTiktok(null);} else {profile.setTiktok(tiktok);}

                if (instagram == null || instagram.trim().isEmpty()) {
                    profile.setInstagram(null);} else {profile.setInstagram(instagram);}

                if (twitch == null || twitch.trim().isEmpty()) {
                    profile.setTwitch(null);} else {profile.setTwitch(twitch);}

                if (youtube == null || youtube.trim().isEmpty()) {
                    profile.setYoutube(null);} else {profile.setYoutube(youtube);}

                String avatarUrl = authService.saveAvatar(avatar, login);
                if (avatarUrl == null) {
                    redirectAttributes.addFlashAttribute("error", "Аватар є обов'язковим");
                    return "redirect:/auth/register";
                }
                profile.setAvatarURL(avatarUrl);

                if (description == null || description.isEmpty()) {
                    redirectAttributes.addFlashAttribute("error", "Опис не може бути порожнім");
                    return "redirect:/auth/register";
                }

                authService.RegisterCreator(user, profile);
                session.setUser(user);
            } else {
                authService.Register(user);
                session.setUser(user);
            }

            return "redirect:/";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/auth/register";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/auth/register";
        }
    }
    @GetMapping("/login")
    public String ShowloginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("login") String login,
                        @RequestParam("password") String password,
                        RedirectAttributes redirectAttributes) {
        try {
            User user = authService.login(login, password);
            if (user == null) {
                redirectAttributes.addFlashAttribute("error", "Неправильний логін або пароль");
                return "redirect:/auth/login";
            }
            session.setUser(user);
            if(user.getRole()==Role.ADMIN){return "redirect:/adminpanel";}
            return "redirect:/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Помилка під час входу: " + e.getMessage());
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        session.clear();
        return "redirect:/";
    }
}
