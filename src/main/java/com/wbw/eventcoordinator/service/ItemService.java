package com.wbw.eventcoordinator.service;

import com.wbw.eventcoordinator.entity.Item;
import com.wbw.eventcoordinator.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public Item createItem(Item item) {
        return itemRepository.save(item);
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Optional<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }

    public Item updateItem(Long id, Item itemDetails) {
        return itemRepository.findById(id).map(item -> {
            item.setName(itemDetails.getName());
            item.setAssignedUser(itemDetails.getAssignedUser());
            return itemRepository.save(item);
        }).orElseThrow(() -> new RuntimeException("Item not found with id " + id));
    }

    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }
}