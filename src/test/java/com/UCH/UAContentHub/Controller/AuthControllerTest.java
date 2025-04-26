package com.UCH.UAContentHub.Controller;

import com.UCH.UAContentHub.Entity.Enum.Role;
import com.UCH.UAContentHub.Entity.User;
import com.UCH.UAContentHub.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private User admin;
    private User user;

    @BeforeEach
    void initData() {
        userRepository.deleteAll();

        admin = new User();
        admin.setName("AdminUser");
        admin.setLogin("admin");
        admin.setPassword("password");
        admin.setEmail("admin@example.com");
        admin.setRegistrationDate(LocalDateTime.now());
        admin.setRole(Role.ADMIN);

        user = new User();
        user.setName("RegularUser");
        user.setLogin("user");
        user.setPassword("password");
        user.setEmail("user@example.com");
        user.setRegistrationDate(LocalDateTime.now());
        user.setRole(Role.USER);

        userRepository.saveAll(List.of(admin, user));
    }

    @Test
    void testLoginAsAdmin() throws Exception {

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("login", "admin")
                        .param("password", "password"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/adminpanel"));
    }
    @Test
    void testLoginAsUser() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("login", "user")
                        .param("password", "password"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testInvalidLogin() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("login", "wrong")
                        .param("password", "password"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/auth/login"));
    }
    @Test
    void testExceptionDuringLogin() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("login", "error")
                        .param("password", "password"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/auth/login"));
    }
}