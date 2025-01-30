package com.UCH.UAContentHub.Service.Implementation;

import com.UCH.UAContentHub.Entity.Profile;
import com.UCH.UAContentHub.Entity.User;
import com.UCH.UAContentHub.Repository.ProfileRepository;
import com.UCH.UAContentHub.Repository.UserRepository;
import com.UCH.UAContentHub.Service.Interface.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;

    private ProfileRepository prRepository;

    private static final String UPLOADED_FOLDER = "src/main/resources/static/avatars/";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"
    );

    public boolean isValidUrl(String url) {
        String regex = "^(https?://)?(www\\.)?([a-zA-Z0-9-]+)\\.[a-zA-Z]{2,6}(/.*)?$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

    @Override
    public User Register(User user) {

        if (!EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            throw new IllegalArgumentException("Некоректна пошта");
        }

        if (userRepository.findByEmail(user.getEmail())!=null) {
            throw new IllegalArgumentException("Користувач з даною поштою існує");}

        if (userRepository.findByLogin(user.getLogin())!=null) {
            throw new IllegalArgumentException("Користувач з даним логіном існує");}

        if (user.getPassword().length() < 8) {
            throw new IllegalArgumentException("Пароль має мати мінімум 8 символів");}

        if (user.getRegistrationDate() == null) {
            user.setRegistrationDate(LocalDateTime.now());}

        return userRepository.save(user);
    }

    @Override
    public void RegisterCreator(User user, Profile profile) {

        if (!EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            throw new IllegalArgumentException("Некоректна пошта");}

        if (userRepository.findByEmail(user.getEmail())!=null) {
            throw new IllegalArgumentException("Користувач з даною поштою існує");}

        if (userRepository.findByLogin(user.getLogin())!=null) {
            throw new IllegalArgumentException("Користувач з даним логіном існує");}

        if (user.getRegistrationDate() == null) {
            user.setRegistrationDate(LocalDateTime.now());}

        if (user.getPassword().length() < 8) {
            throw new IllegalArgumentException("Пароль має мати мінімум 8 символів");}

        if (profile.getDescription() == null || profile.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Опис профілю не має бути порожнім");}

        if (profile.getTiktok() == null && profile.getInstagram() == null
                && profile.getTwitch() == null && profile.getYoutube() == null) {
            throw new IllegalArgumentException("Творець повинен мати принаймні одну пов’язану соціальну мережу.");}

        if (profile.getTiktok() != null && !profile.getTiktok().isEmpty()) {

            if (prRepository.existsByTiktok(profile.getTiktok())) {
                throw new IllegalArgumentException("Це посилання на TikTok вже використовується");}

            if (!isValidUrl(profile.getTiktok())) {
                throw new IllegalArgumentException("Некоректне посилання на TikTok");}
        }

        if (profile.getInstagram() != null && !profile.getInstagram().isEmpty()) {

            if (prRepository.existsByInstagram(profile.getInstagram())) {
                throw new IllegalArgumentException("Це посилання на Instagram вже використовується");}

            if (!isValidUrl(profile.getInstagram())) {
                throw new IllegalArgumentException("Некоректне посилання на Instagram");}
        }

        if (profile.getTwitch() != null && !profile.getTwitch().isEmpty()) {

            if (prRepository.existsByTwitch(profile.getTwitch())) {
                throw new IllegalArgumentException("Це посилання на Twitch вже використовується");}

            if (!isValidUrl(profile.getTwitch())) {
                throw new IllegalArgumentException("Некоректне посилання на Twitch");}
        }

        if (profile.getYoutube() != null && !profile.getYoutube().isEmpty()) {

            if (prRepository.existsByYoutube(profile.getYoutube())) {
                throw new IllegalArgumentException("Це посилання на YouTube вже використовується");}

            if (!isValidUrl(profile.getYoutube())) {
                throw new IllegalArgumentException("Некоректне посилання на YouTube");}
        }

        profile.setUser(user);
        user.setProfile(profile);

        userRepository.save(user);
        prRepository.save(profile);
    }


    @Override
    public User login(String login, String password) {
        User user = userRepository.findByLoginAndPassword(login, password);

        if (user == null) {
            throw new IllegalArgumentException("Невірний логін або пароль");
        }

        return user;
    }
    @Override
    public String saveAvatar(MultipartFile avatar, String login) {

        if (avatar == null || avatar.isEmpty()) {
            return null;
        }

        try {
            String originalFilename = avatar.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));

            if (!extension.equalsIgnoreCase(".jpg") && !extension.equalsIgnoreCase(".jpeg") && !extension.equalsIgnoreCase(".png")) {
                throw new IllegalArgumentException("Невірний формат файлу. Підтримуються лише .jpg, .jpeg, .png");
            }

            Path uploadPath = Paths.get(UPLOADED_FOLDER);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String avatarFileName = login + extension;
            Path filePath = uploadPath.resolve(avatarFileName);

            Files.write(filePath, avatar.getBytes());

            return "/avatars/" + avatarFileName;
        } catch (Exception e) {
            throw new RuntimeException("Помилка при завантаженні аватара: " + e.getMessage());
        }
    }
}
