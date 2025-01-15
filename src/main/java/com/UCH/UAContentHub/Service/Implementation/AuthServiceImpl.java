package com.UCH.UAContentHub.Service.Implementation;

import com.UCH.UAContentHub.Entity.Profile;
import com.UCH.UAContentHub.Entity.User;
import com.UCH.UAContentHub.Repository.ProfileRepository;
import com.UCH.UAContentHub.Repository.UserRepository;
import com.UCH.UAContentHub.Service.Interface.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfileRepository prRepository;
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
            throw new IllegalArgumentException("Користувач з даною поштою існує");
        }
        if (userRepository.findByLogin(user.getLogin())!=null) {
            throw new IllegalArgumentException("Користувач з даним логіном існує");
        }
        if (user.getPassword().length() < 8) {
            throw new IllegalArgumentException("Пароль має мати мінімум 8 символів");
        }
        if (user.getRegistrationDate() == null) {
            user.setRegistrationDate(LocalDateTime.now());
        }

        return userRepository.save(user);
    }

    @Override
    public void RegisterCreator(User user, Profile profile) {

        if (!EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            throw new IllegalArgumentException("Некоректна пошта");
        }

        if (userRepository.findByEmail(user.getEmail())!=null) {
            throw new IllegalArgumentException("Користувач з даною поштою існує");
        }
        if (userRepository.findByLogin(user.getLogin())!=null) {
            throw new IllegalArgumentException("Користувач з даним логіном існує");
        }
        if (user.getRegistrationDate() == null) {
            user.setRegistrationDate(LocalDateTime.now());
        }
        if (user.getPassword().length() < 8) {
            throw new IllegalArgumentException("Пароль має мати мінімум 8 символів");
        }
        if (profile.getDescription() == null || profile.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Опис профілю не має бути порожнім");
        }
        if (profile.getTiktok() != null && !profile.getTiktok().isEmpty() && !isValidUrl(profile.getTiktok())) {
            throw new IllegalArgumentException("Некоректне посилання на TikTok");
        }
        if (profile.getInstagram() != null && !profile.getInstagram().isEmpty() && !isValidUrl(profile.getInstagram())) {
            throw new IllegalArgumentException("Некоректне посилання на Instagram");
        }
        if (profile.getTwitch() != null && !profile.getTwitch().isEmpty() && !isValidUrl(profile.getTwitch())) {
            throw new IllegalArgumentException("Некоректне посилання на Twitch");
        }
        if (profile.getYoutube() != null && !profile.getYoutube().isEmpty() && !isValidUrl(profile.getYoutube())) {
            throw new IllegalArgumentException("Некоректне посилання на YouTube");
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
}
