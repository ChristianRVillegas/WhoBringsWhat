//package com.wbw.eventcoordinator.controller;
//
//import com.wbw.eventcoordinator.entity.Event;
//import com.wbw.eventcoordinator.entity.User;
//import com.wbw.eventcoordinator.service.EventService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.Arrays;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(EventController.class)
//public class EventControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private EventService eventService;
//
//    @Test
//    public void testCreateEvent() throws Exception {
//        User organizerA = new User("organizer", "organizerA@example.com", "password", "profilePic", "bio");
//        Event eventA = new Event("Poker Night", "A fun poker night", LocalDate.of(2023, 6, 17).toString(), LocalTime.of(18, 0).toString(), organizerA);
//        when(eventService.createEvent(any(Event.class))).thenReturn(eventA);
//
//        mockMvc.perform(post("/api/events")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\": \"Poker Night\", \"date\": \"2023-06-17\", \"time\": \"18:00:00\", \"description\": \"A fun poker night\"}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Poker Night"));
//    }
//
//    @Test
//    public void testGetAllEvents() throws Exception {
//        User organizerA = new User("organizer", "organizerA@example.com", "password", "profilePic", "bio");
//        User organizerB = new User("organizer", "organizerB@example.com", "password", "profilePic", "bio");
//        Event eventA = new Event("EventA", "description", LocalDate.of(2023, 6, 17).toString(), LocalTime.of(18, 0).toString(), organizerA);
//        Event eventB = new Event("EventB", "description", LocalDate.of(2023, 6, 17).toString(), LocalTime.of(18, 0).toString(), organizerB);
//        when(eventService.getAllEvents()).thenReturn(Arrays.asList(eventA, eventB));
//
//        mockMvc.perform(get("/api/events")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()").value(2))
//                .andExpect(jsonPath("$[0].").value(1));
//    }
//}