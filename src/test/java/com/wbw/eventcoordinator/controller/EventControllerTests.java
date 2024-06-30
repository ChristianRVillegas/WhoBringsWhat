package com.wbw.eventcoordinator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wbw.eventcoordinator.entity.Event;
import com.wbw.eventcoordinator.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventController.class)
public class EventControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Autowired
    private ObjectMapper objectMapper;

    public Event eventA, eventB;



    @BeforeEach
    public void setup() {
        eventA = new Event("EventA", "description1", LocalDate.of(2023, 6, 17).toString(), LocalTime.of(18, 0).toString(), null);
        eventA.setId(1L);

        eventB = new Event("EventB", "description2", LocalDate.of(2023, 6, 17).toString(), LocalTime.of(18, 0).toString(), null);
        eventB.setId(2L);

        when(eventService.createEvent(any(Event.class))).thenReturn(eventA);
        when(eventService.getEventById(1L)).thenReturn(Optional.of(eventA));
        when(eventService.getAllEvents()).thenReturn(Arrays.asList(eventA, eventB));
        doNothing().when(eventService).deleteEventById(1L);
    }

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    public void testCreateEvent() throws Exception {
        Event eventC = new Event("Event3", "description3", LocalDate.of(2023, 6, 17).toString(), LocalTime.of(18, 0).toString(), null);;
        eventC.setId(3L);

        when(eventService.createEvent(any(Event.class))).thenReturn(eventC);

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventC))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // Add CSRF token
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Event3"))
                .andExpect(jsonPath("$.description").value("description3"));

        verify(eventService).createEvent(any(Event.class));
    }

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    public void testGetEventById() throws Exception {
        mockMvc.perform(get("/api/events/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("EventA"));
    }

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    public void testGetAllEvents() throws Exception {
        mockMvc.perform(get("/api/events")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("EventA"))
                .andExpect(jsonPath("$[1].name").value("EventB"));
    }

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    public void testGetEventByName() throws Exception {

        List<Event> mockList = new ArrayList<Event>();

        mockList.add(eventA);
        mockList.add(eventB);


        when(eventService.getEventByName("cornball")).thenReturn(mockList);

        mockMvc.perform(get("/api/events/name/cornball")
                .contentType(MediaType.APPLICATION_JSON).
                        with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("EventA"))
                .andExpect(jsonPath("$[1].name").value("EventB"));
    }

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    public void testDeleteEventById() throws Exception {
        mockMvc.perform(delete("/api/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // Add CSRF token
                .andExpect(status().isNoContent());

        verify(eventService, times(1)).deleteEventById(1L);
    }
}