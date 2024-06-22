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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
}