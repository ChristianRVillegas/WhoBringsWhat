package com.wbw.eventcoordinator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wbw.eventcoordinator.entity.User;
import com.wbw.eventcoordinator.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        // Create mock users
        User user1 = new User("user1", "user1@example.com", "password1", "profilePic1", "bio1");
        user1.setId(1L);

        User user2 = new User("user2", "user2@example.com", "password2", "profilePic2", "bio2");
        user2.setId(2L);

        // Mock the user service responses
        when(userService.createUser(any(User.class))).thenReturn(user1);
        when(userService.getUserById(1L)).thenReturn(Optional.of(user1));
        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));
    }

    @Test
    @WithMockUser(username = "user1", password = "password1", roles = "USER")
    public void testCreateUser() throws Exception {
        User user = new User("user3", "user3@example.com", "password3", "profilePic3", "bio3");

        mockMvc.perform(post("/api/users")
                        .with(httpBasic("user1", "password1")) // Use valid credentials
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"));
    }

    @Test
    @WithMockUser(username = "user1", password = "password1", roles = "USER")
    public void testGetUserById() throws Exception {
        mockMvc.perform(get("/api/users/1")
                        .with(httpBasic("user1", "password1")) // Use valid credentials
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"));
    }

    @Test
    @WithMockUser(username = "user1", password = "password1", roles = "USER")
    public void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/users")
                        .with(httpBasic("user1", "password1")) // Use valid credentials
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].username").value("user2"));
    }

    @Test
    @WithMockUser(username = "user1", password = "password1", roles = "USER")
    public void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/1")
                        .with(httpBasic("user1", "password1")) // Use valid credentials
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}