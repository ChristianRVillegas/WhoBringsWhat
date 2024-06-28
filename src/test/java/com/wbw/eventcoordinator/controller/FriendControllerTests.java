//package com.wbw.eventcoordinator.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.wbw.eventcoordinator.entity.Friend;
//import com.wbw.eventcoordinator.entity.FriendId;
//import com.wbw.eventcoordinator.service.FriendService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Arrays;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(FriendController.class)
//public class FriendControllerTests {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private FriendService friendService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private Friend friend1;
//    private Friend friend2;
//
//    @BeforeEach
//    public void setup() {
//        FriendId friendId1 = new FriendId(1L, 2L);
//        friend1 = new Friend(friendId1, null);
//
//        FriendId friendId2 = new FriendId(2L, 3L);
//        friend2 = new Friend(friendId2, null);
//
//        when(friendService.addFriend(any(Friend.class).getUser().getId(),any(Friend.class).getFriend().getId())).thenReturn(friend1);
//        when(friendService.getFriendById(friendId1)).thenReturn(Optional.of(friend1));
//        when(friendService.getAllFriends()).thenReturn(Arrays.asList(friend1, friend2));
//        doNothing().when(friendService).deleteFriend(friendId1);
//    }
//
//    @Test
//    @WithMockUser(username = "user1", roles = "USER")
//    public void testCreateFriend() throws Exception {
//        Friend newFriend = new Friend(new FriendId(3L, 4L), null);
//
//        when(friendService.createFriend(any(Friend.class))).thenReturn(newFriend);
//
//        mockMvc.perform(post("/api/friends")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(newFriend))
//                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // Add CSRF token
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.friendId.userId").value(3))
//                .andExpect(jsonPath("$.friendId.friendId").value(4));
//
//        verify(friendService, times(1)).createFriend(any(Friend.class));
//    }
//
//    @Test
//    @WithMockUser(username = "user1", roles = "USER")
//    public void testGetFriendById() throws Exception {
//        mockMvc.perform(get("/api/friends/1/2")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.friendId.userId").value(1))
//                .andExpect(jsonPath("$.friendId.friendId").value(2));
//    }
//
//    @Test
//    @WithMockUser(username = "user1", roles = "USER")
//    public void testGetAllFriends() throws Exception {
//        mockMvc.perform(get("/api/friends")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()").value(2))
//                .andExpect(jsonPath("$[0].friendId.userId").value(1))
//                .andExpect(jsonPath("$[1].friendId.userId").value(2));
//    }
//
//    @Test
//    @WithMockUser(username = "user1", roles = "USER")
//    public void testDeleteFriend() throws Exception {
//        mockMvc.perform(delete("/api/friends/1/2")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // Add CSRF token
//                .andExpect(status().isNoContent());
//
//        verify(friendService, times(1)).deleteFriend(new FriendId(1L, 2L));
//    }
//}