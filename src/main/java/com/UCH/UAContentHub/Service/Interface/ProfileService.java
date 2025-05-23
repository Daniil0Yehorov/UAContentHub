package com.UCH.UAContentHub.Service.Interface;
import com.UCH.UAContentHub.Entity.Profile;
import com.UCH.UAContentHub.Entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {
    //мб убрать  і додати у майбутній сервіс АдмінСервіс
    void placeTagsForProfile(int profileId,String[] tags);
    //оновлення даних профілю креатора. !!!ПРИ ОНОВЛЕННІ СТАТУС БУДЕ НЕПЕРЕВІРЕНИЙ, БО ЗМІНЮЮТЬСЯ ПОСИЛАННЯ
    Profile updateP(Profile updatedProfile);
    //репорт на профіль. Репорт за пост, звісно буде в іншому сервісі
    void reportProfile(int profileid,int whoComplainedId,String reason);
    //трігери у бд зменшують або збільшують кількість підписок у профілі креатора
    void subscribeCreator(Profile profileToSubscribe, User subscriber);
    void unsubscribeCreator(Profile profileToUnsubscribe, User unsubscriber);
    //оновлення даних користувача
    User updateU(User user);
    //додатково
    Profile getProfileByID(int id);
    boolean isSubscribed(Profile profile, User user);
    //встановлення нового аватару для юзера
    String uploadAvatar(User user, MultipartFile avatar);
    boolean hasUserReportedProfile(int userId,int creatorUserId);
}
