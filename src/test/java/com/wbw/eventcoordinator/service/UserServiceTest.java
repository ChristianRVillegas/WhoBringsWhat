//package com.wbw.eventcoordinator.service;
//
//import com.wbw.eventcoordinator.controller.EventController;
//import com.wbw.eventcoordinator.entity.User;
//import com.wbw.eventcoordinator.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//
//@WebMvcTest(UserService.class)
//public class UserServiceTest {
//
//    @InjectMocks
//    private UserService userService;
//
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @BeforeEach
//    public void setup() {
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//
//    }
//
//    public UserServiceTest() {
//        MockitoAnnotations.openMocks(this);
//
//
//    }
//
//    @Test
//    public void testCreateUser() throws Exception {
//        User user = new User("testuser", "test@example.com", "password", "profilePic", "bio");
//        when(userRepository.save(any(User.class))).thenReturn(user);
//
//        mockMvc.perform(post("/api/events")
//                        .with(httpBasic("username", "password"))); // Replace with valid credentials
//        assertNotNull(createdUser);
//        assertEquals("testuser", createdUser.getUsername());
//        verify(userRepository, times(1)).save(user);
//    }
//
//    @Test
//    public void testGetUserById() {
//        User user = new User("testuser", "test@example.com", "password", "profilePic", "bio");
//        user.setId(1L);
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//
//        Optional<User> foundUser = userService.getUserById(1L);
//
//        assertNotNull(foundUser);
//        assertEquals("testuser", foundUser.get().getUsername());
//        verify(userRepository, times(1)).findById(1L);
//    }
//}