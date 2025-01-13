package com.UCH.UAContentHub.Service.Interface;


import com.UCH.UAContentHub.Entity.Profile;
import com.UCH.UAContentHub.Entity.User;


public interface AuthService {
    //реєстрація звичайного користувача у системі
    User Register(User user);
    //реєстрація креатора у системі, але зі статусом "не перевірено"(поки адміністратор не перевірить
    // цього креатора, та не поставить інший статус- креатор не зможе робити ніяких дій)
    void RegisterCreator(User user, Profile profile);
    //оновлення даних користувача
    User update(User user);
    //авторизація
    User login(String login, String password);
    //валідація юрл посилання
    public boolean isValidUrl(String url);

}
