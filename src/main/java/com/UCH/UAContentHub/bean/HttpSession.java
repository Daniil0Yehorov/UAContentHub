package com.UCH.UAContentHub.bean;

import com.UCH.UAContentHub.Entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class HttpSession {
    @Getter
    @Setter
    private User user;

    public boolean isPresent() {
        return user != null;
    }
    public void clear() {
        clearUser();
    }
    public void clearUser() {
        user = null;
    }
}