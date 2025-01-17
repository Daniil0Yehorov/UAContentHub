package com.UCH.UAContentHub.Service.Implementation;

import com.UCH.UAContentHub.Entity.*;
import com.UCH.UAContentHub.Entity.Enum.ComplaintStatus;
import com.UCH.UAContentHub.Entity.Enum.CreatorProfileStatus;
import com.UCH.UAContentHub.Repository.*;
import com.UCH.UAContentHub.Service.Interface.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private ProfileRepository profileRepository;

    private UserRepository userRepository;

    private Profile_has_tagsRepository PHSserviceRepository;

    private TagsRepository tagsRepository;

    private СomplaintRepository complaintRepository;

    private SubscriptionRepository subcriptionRepository;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"
    );

    //в теорії спрацює, але перевірити треба коли буде розроблен функціонал адміна
    @Override
    public void placeTagsForProfile(int profileId, String[] tags) {
        Profile profile = profileRepository.findById(profileId).orElse(null);
        if (profile == null) {
            throw new IllegalArgumentException("Профіль не знайден");
        }

        for (String tagName : tags) {
            Tags tag = tagsRepository.findByName(tagName);
            if (tag == null) {
                throw new IllegalArgumentException("Тег не знайден: " + tagName);
            }

            Profile_has_tags profileHasTag = new Profile_has_tags();
            profileHasTag.setProfile(profile);
            profileHasTag.setTags(tag);
            PHSserviceRepository.save(profileHasTag);
        }
    }

    @Override
    public Profile updateP(Profile updatedProfile) {
        Profile existingProfile = profileRepository.findById(updatedProfile.getId()).orElse(null);
        if (existingProfile == null) {
            throw new IllegalArgumentException("профіль не знайден");
        }
        if (updatedProfile.getDescription() == null || updatedProfile.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Опис не може бути порожнім");
        }

        if (!isValidUrl(updatedProfile.getAvatarURL())) {
            throw new IllegalArgumentException("Avatar URL не валідний");
        }
        if (!isValidUrl(updatedProfile.getTiktok())) {
            throw new IllegalArgumentException("не валідний TikTok URL");
        }

        if (!isValidUrl(updatedProfile.getInstagram())) {
            throw new IllegalArgumentException("не валідний Instagram URL");
        }

        if (!isValidUrl(updatedProfile.getTwitch())) {
            throw new IllegalArgumentException("не валідний Twitch URL");
        }

        if (!isValidUrl(updatedProfile.getYoutube())) {
            throw new IllegalArgumentException("не валідний YouTube URL");
        }
        if (updatedProfile.getDescription() != null) {
            existingProfile.setDescription(updatedProfile.getDescription());
        }
        if (updatedProfile.getAvatarURL() != null) {
            existingProfile.setAvatarURL(updatedProfile.getAvatarURL());
        }
        if (updatedProfile.getTiktok() != null) {
            existingProfile.setTiktok(updatedProfile.getTiktok());
        }
        if (updatedProfile.getInstagram() != null) {
            existingProfile.setInstagram(updatedProfile.getInstagram());
        }
        if (updatedProfile.getTwitch() != null) {
            existingProfile.setTwitch(updatedProfile.getTwitch());
        }
        if (updatedProfile.getYoutube() != null) {
            existingProfile.setYoutube(updatedProfile.getYoutube());
        }

        existingProfile.setStatus(CreatorProfileStatus.UNCONFIRMED);
        //рейтинг буде додаватись при 5-10 ревью на креатора, а  поки його не буде
        //кількість підписників збільшується за допомогою трігера у бд
        return profileRepository.save(existingProfile);
    }

    @Override
    public void reportProfile(int profileid,int whoComplainedId,String reason) {
        Profile reportedProfile = profileRepository.findById(profileid).orElse(null);
        if (reportedProfile == null) {
            throw new IllegalArgumentException("Профіль не знайден");
        }

        User complainingUser = userRepository.findById(whoComplainedId)
                .orElseThrow(() -> new IllegalArgumentException("Користувач не знайден"));

        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Причина не може бути порожньою");
        }

        Complaint newComplaint = new Complaint();
        newComplaint.setProfile(reportedProfile);
        newComplaint.setUser(complainingUser);
        newComplaint.setReason(reason);
        newComplaint.setStatus(ComplaintStatus.PENDING);
        complaintRepository.save(newComplaint);
    }

    @Override
    public void subscribeCreator(Profile profileToSubscribe, User subscriber) {
        if (profileToSubscribe == null || subscriber == null) {
            throw new IllegalArgumentException("Креатор або підписник не можуть бути порожніми");
        }

        User creator = userRepository.findById(profileToSubscribe.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("Креатор не знайден"));

        User subscribingUser = userRepository.findById(subscriber.getId())
                .orElseThrow(() -> new IllegalArgumentException("Підписник не знайден"));

        if (subcriptionRepository.findByCreator_IdAndUser_Id(creator.getId(), subscribingUser.getId()) != null) {
            throw new IllegalArgumentException("Вже підписан на креатора");
        }

        Subscription newSubscription = new Subscription();
        newSubscription.setUser(subscribingUser);
        newSubscription.setCreator(creator);
        newSubscription.setSubscriptionDate(LocalDateTime.now());

        subcriptionRepository.save(newSubscription);

    }
    @Override
    public User updateU(User user) {

        if (user.getEmail() != null && !EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            throw new IllegalArgumentException("Некоректна пошта");
        }

        if (user.getEmail() != null) {
            User existingUserByEmail = userRepository.findByEmail(user.getEmail());
            if (existingUserByEmail != null && existingUserByEmail.getId() != user.getId()) {
                throw new IllegalArgumentException("Користувач з даною поштою існує");
            }
        }

        if (user.getLogin() != null) {
            User existingUserByLogin = userRepository.findByLogin(user.getLogin());
            if (existingUserByLogin != null && existingUserByLogin.getId() != user.getId()) {
                throw new IllegalArgumentException("Користувач з даним логіном існує");
            }
        }

        if (user.getPassword() != null && user.getPassword().length() < 8) {
            throw new IllegalArgumentException("Пароль має бути не менше 8 символів");
        }

        return userRepository.save(user);
    }
    @Override
    public void unsubscribeCreator(Profile profileToUnsubscribe, User unsubscriber) {
        if (profileToUnsubscribe == null || unsubscriber == null) {
            throw new IllegalArgumentException("Профіль або відписника не можуть бути нульовими");
        }

        User creator = userRepository.findById(profileToUnsubscribe.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("Креатор не знайден"));

        User unsubscribingUser = userRepository.findById(unsubscriber.getId())
                .orElseThrow(() -> new IllegalArgumentException("Відписник не знайден"));

        Subscription existingSubscription = subcriptionRepository.findByCreator_IdAndUser_Id(creator.getId(),
                unsubscribingUser.getId());
        if (existingSubscription == null) {
            throw new IllegalArgumentException("Немає підписки задля відписки");
        }

        subcriptionRepository.delete(existingSubscription);
    }
    //дублюється, щоб не використовувати фунцію з іншого сервісу
    @Override
    public Profile getProfileByID(int id) {return profileRepository.findById(id).orElse(null);}

    private boolean isValidUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return true;
        }
        return url.matches("^(http|https)://.*$");
    }
    public boolean isSubscribed(Profile profile, User user) {
        if (profile == null || user == null) {
            throw new IllegalArgumentException("Профіль або користувач не можуть бути порожніми");
        }

        return subcriptionRepository.findByCreator_IdAndUser_Id(profile.getUser().getId(), user.getId()) != null;
    }
}
