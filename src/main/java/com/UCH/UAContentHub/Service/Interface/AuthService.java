package com.UCH.UAContentHub.Service.Interface;


import com.UCH.UAContentHub.Entity.Profile;
import com.UCH.UAContentHub.Entity.User;
import org.springframework.web.multipart.MultipartFile;


public interface AuthService {
    //реєстрація звичайного користувача у системі
    User Register(User user);
    //реєстрація креатора у системі, але зі статусом "не перевірено"(поки адміністратор не перевірить
    // цього креатора, та не поставить інший статус- креатор не зможе робити ніяких дій)
    void RegisterCreator(User user, Profile profile);
    //авторизація
    User login(String login, String password);
    //валідація юрл посилання
    boolean isValidUrl(String url);
    //додавання аватару користувача
    String saveAvatar(MultipartFile avatar, String login);
}
