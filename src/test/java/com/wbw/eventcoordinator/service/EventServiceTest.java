package com.wbw.eventcoordinator.service;

import com.wbw.eventcoordinator.entity.Event;
import com.wbw.eventcoordinator.entity.User;
import com.wbw.eventcoordinator.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private EventRepository eventRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    public EventServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateEvent() {
        User organizer = new User("organizer", "organizer@example.com", "password", "profilePic", "bio");
        Event event = new Event("Poker Night", "A night for the boys", LocalDate.of(2023, 6, 17).toString(), LocalTime.of(18, 0).toString(), organizer);
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        Event createdEvent = eventService.createEvent(event);

        assertNotNull(createdEvent);
        assertEquals("Poker Night", createdEvent.getName());
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    public void testGetEventById() {
        User organizer = new User("organizer", "organizer@example.com", "password", "profilePic", "bio");
        Event event = new Event("Poker Night", "A night for the boys", LocalDate.of(2023, 6, 17).toString(), LocalTime.of(18, 0).toString(), organizer);
        event.setId(1L);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        Optional<Event> foundEvent = eventService.getEventById(1L);

        assertNotNull(foundEvent);
        assertEquals("Poker Night", foundEvent.get().getName());
        verify(eventRepository, times(1)).findById(1L);
    }

    @Test
    public void testDeleteEventById(){

        User organizer = new User("organizer", "organizer@example.com", "password", "profilePic", "bio");
        Event event = new Event("Poker Night", "A night for the boys", LocalDate.of(2023, 6, 17).toString(), LocalTime.of(18, 0).toString(), organizer);
        event.setId(12L);
        doNothing().when(eventRepository).deleteById(event.getId());
        eventService.deleteEventById(12L);

        verify(eventRepository).deleteById(event.getId());
    }

    @Test
    public void testGetEventByName(){
        User organizer = new User("organizer", "organizer@example.com", "password", "profilePic", "bio");
        Event event = new Event("Poker Night", "A night for the boys", LocalDate.of(2023, 6, 17).toString(), LocalTime.of(18, 0).toString(), organizer);
        event.setId(12L);
        List<Event> mockList = new ArrayList<Event>();
        mockList.add(event);

        when(eventRepository.findByName(anyString())).thenReturn(mockList);

        List<Event> response = eventService.getEventByName("Poker Night");
        assertEquals(1, response.size());
        assertEquals("Poker Night", response.get(0).getName());


    }

    @Test
    public void testGetAllEvents(){
        User organizer = new User("organizer", "organizer@example.com", "password", "profilePic", "bio");
        Event eventA = new Event("Poker Night 1", "A night for the boys", LocalDate.of(2023, 6, 17).toString(), LocalTime.of(18, 0).toString(), organizer);
        Event eventB = new Event("Poker Night 2", "A night for the boys", LocalDate.of(2023, 6, 17).toString(), LocalTime.of(18, 0).toString(), organizer);
        eventA.setId(13L);
        eventB.setId(12L);

        List<Event> mockList = new ArrayList<Event>();
        mockList.add(eventA);
        mockList.add(eventB);

        when(eventRepository.findAll()).thenReturn(mockList);
        List<Event> response = eventService.getAllEvents();
        assertEquals(2, response.size());
        assertEquals("Poker Night 1", response.get(0).getName());
        assertEquals("Poker Night 2", response.get(1).getName());

        verify(eventRepository).findAll();
    }



}