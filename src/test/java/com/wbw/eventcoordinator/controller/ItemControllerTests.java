package com.wbw.eventcoordinator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wbw.eventcoordinator.entity.Item;
import com.wbw.eventcoordinator.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
public class ItemControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    private Item item1;
    private Item item2;

    @BeforeEach
    public void setup() {
        item1 = new Item("Item1", "Description1", null, null);
        item1.setId(1L);

        item2 = new Item("Item2", "Description2", null, null);
        item2.setId(2L);

        when(itemService.createItem(any(Item.class))).thenReturn(item1);
        when(itemService.getItemById(1L)).thenReturn(Optional.of(item1));
        when(itemService.getAllItems()).thenReturn(Arrays.asList(item1, item2));
        doNothing().when(itemService).deleteItem(1L);
    }

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    public void testCreateItem() throws Exception {
        Item newItem = new Item("Item3", "Description3", null, null);
        newItem.setId(3L);

        when(itemService.createItem(any(Item.class))).thenReturn(newItem);

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newItem))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // Add CSRF token
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Item3"))
                .andExpect(jsonPath("$.description").value("Description3"));

        verify(itemService, times(1)).createItem(any(Item.class));
    }

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    public void testGetItemById() throws Exception {
        mockMvc.perform(get("/api/items/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Item1"));
    }

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    public void testGetAllItems() throws Exception {
        mockMvc.perform(get("/api/items")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Item1"))
                .andExpect(jsonPath("$[1].name").value("Item2"));
    }

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    public void testDeleteItem() throws Exception {
        mockMvc.perform(delete("/api/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // Add CSRF token
                .andExpect(status().isNoContent());

        verify(itemService, times(1)).deleteItem(1L);
    }
}