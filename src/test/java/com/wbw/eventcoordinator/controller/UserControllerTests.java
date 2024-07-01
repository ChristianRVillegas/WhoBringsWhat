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

import javax.print.attribute.standard.Media;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    public User user1;
    public User user2;

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
    public void testCreateUser_Success() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(user1);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.email").value("user1@example.com"));

        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    public void testGetUserById_Success() throws Exception {
        when(userService.getUserById(anyLong())).thenReturn(Optional.ofNullable(user1));

        mockMvc.perform(get("/api/users/id/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"));
    }

    @Test
    public void testGetAllUsers_Success() throws Exception {
        List<User> mockList = new ArrayList<>();
        mockList.add(user1);
        mockList.add(user2);

        when(userService.getAllUsers()).thenReturn(mockList);

        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].username").value("user2"));
    }

    @Test
    public void testDeleteUser_Success() throws Exception {
        doNothing().when(userService).deleteUser(anyLong());
        mockMvc.perform(delete("/api/users/id/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    public void testGetUserByName_Success() throws Exception {
        List<User> mockList = new ArrayList<>();
        mockList.add(user1);
        mockList.add(user2);
        when(userService.getUserByName(anyString())).thenReturn(mockList);

        mockMvc.perform(get("/api/users/name/user")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].username").value("user2"));

    }

    @Test
    @WithMockUser(username = "admin", roles = "admin")
    public void testGetUserByEmail_Success() throws Exception {
        when(userService.getUserByEmail(anyString())).thenReturn(user1);

        mockMvc.perform(get("/api/users/email/user1@example.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


    }

    @Test
    @WithMockUser(username = "admin", roles = "admin")
    public void testUpdateUser_Success() throws Exception {
        when(userService.updateUser(anyLong(), any(User.class))).thenReturn(user2);

        mockMvc.perform(put("/api/users/id/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user2"));
    }

}