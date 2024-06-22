package com.wbw.eventcoordinator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wbw.eventcoordinator.entity.User;
import com.wbw.eventcoordinator.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTests {


    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user1;
    private User user2;

    @BeforeEach
    public void setup() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Create mock users
        user1 = new User("user1", "user1@example.com", "password1", "profilePic1", "bio1");
        user1.setId(1L);

        user2 = new User("user2", "user2@example.com", "password2", "profilePic2", "bio2");
        user2.setId(2L);

        // Mock the user service responses
        when(userService.createUser(any(User.class))).thenReturn(user1);
        when(userService.getUserById(1L)).thenReturn(Optional.of(user1));
        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));
        doNothing().when(userService).deleteUser(1L);
    }

    @Test
    @WithMockUser(username = "admin", roles = "admin")
    public void testCreateUser() throws Exception {
        User newUser = new User("user3", "user3@example.com", "password3", "profilePic3", "bio3");
        newUser.setId(3L);

        when(userService.createUser(any(User.class))).thenReturn(newUser);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user3"))
                .andExpect(jsonPath("$.email").value("user3@example.com"));

        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = "admin")
    public void testGetUserById() throws Exception {
        mockMvc.perform(get("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "admin")
    public void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].username").value("user2"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "admin")
    public void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1L);
    }
}