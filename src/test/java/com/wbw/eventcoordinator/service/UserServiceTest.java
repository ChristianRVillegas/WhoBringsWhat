package com.wbw.eventcoordinator.service;

import com.wbw.eventcoordinator.controller.EventController;
import com.wbw.eventcoordinator.entity.Event;
import com.wbw.eventcoordinator.entity.User;
import com.wbw.eventcoordinator.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUser() throws Exception {
        User user = new User("testuser", "test@example.com", "password", "profilePic", "bio");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User response = userService.createUser(user);

        assertEquals("testuser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());

        verify(userRepository).save(user);
    }

    @Test
    public void testGetUserById() {
        User user = new User("testuser", "test@example.com", "password", "profilePic", "bio");
        user.setId(1L);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.getUserById(1L);

        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.get().getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetAllUsers() {
        User userA = new User("testuserA", "test@example.com", "password", "profilePic", "bio");
        userA.setId(1L);

        User userB = new User("testuserB", "test@example.com", "password", "profilePic", "bio");
        userB.setId(2L);

        List<User> mockList = new ArrayList<User>();
        mockList.add(userA);
        mockList.add(userB);

        when(userRepository.findAll()).thenReturn(mockList);

        List<User> response = userService.getAllUsers();

        assertEquals(2, response.size());
        assertEquals("testuserA", response.get(0).getUsername());
        assertEquals("testuserB", response.get(1).getUsername());

        verify(userRepository).findAll();
    }

    @Test
    public void testGetUsersByName() {
        User userA = new User("testuserA", "test@example.com", "password", "profilePic", "bio");
        userA.setId(1L);

        User userB = new User("testuserB", "test@example.com", "password", "profilePic", "bio");
        userB.setId(2L);

        List<User> mockList = new ArrayList<User>();
        mockList.add(userA);
        mockList.add(userB);
        when(userRepository.findByUsername(anyString())).thenReturn(mockList);

        List<User> response = userService.getUserByName("testuser");

        assertEquals(2, response.size());
        assertEquals("testuserA", response.get(0).getUsername());
        assertEquals("testuserB", response.get(1).getUsername());

        verify(userRepository).findByUsername("testuser");
    }

    @Test
    public void testGetUserByEmail() {
        User userA = new User("testuserA", "test1@example.com", "password", "profilePic", "bio");
        userA.setId(1L);

        when(userRepository.findByEmail(anyString())).thenReturn(userA);

        User response = userService.getUserByEmail("test1@example.com");

        assertEquals("testuserA", response.getUsername());
        assertEquals("test1@example.com", response.getEmail());

        verify(userRepository).findByEmail("test1@example.com");
    }

    @Test
    public void testUpdateUser() {
        User user = new User("testuser", "test@example.com", "password", "profilePic", "bio");
        user.setId(1L);

        User updatedUser = new User("testuser-alpha", "test@example.com", "password", "profilePic", "bio");
        user.setId(1L);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User response = userService.updateUser(1L, updatedUser);

        assertEquals("testuser-alpha", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
        assertNotEquals("testuser", response.getUsername());

        verify(userRepository).findById(1L);
    }

    @Test
    public void testDeleteUser() {
        User user = new User("testuser", "test@example.com", "password", "profilePic", "bio");
        user.setId(1L);

        doNothing().when(userRepository).deleteById(anyLong());
        userService.deleteUser(1L);
        verify(userRepository).deleteById(1L);
    }
}