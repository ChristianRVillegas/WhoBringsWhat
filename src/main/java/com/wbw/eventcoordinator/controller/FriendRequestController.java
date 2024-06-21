package com.wbw.eventcoordinator.controller;

import com.wbw.eventcoordinator.entity.FriendRequest;
import com.wbw.eventcoordinator.service.FriendRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/friend-requests")
public class FriendRequestController {

    @Autowired
    private FriendRequestService friendRequestService;

    @PostMapping
    public FriendRequest createFriendRequest(@RequestBody FriendRequest friendRequest) {
        return friendRequestService.createFriendRequest(friendRequest);
    }

    @GetMapping
    public List<FriendRequest> getAllFriendRequests() {
        return friendRequestService.getAllFriendRequests();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FriendRequest> getFriendRequestById(@PathVariable Long id) {
        Optional<FriendRequest> friendRequest = friendRequestService.getFriendRequestById(id);
        return friendRequest.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<FriendRequest> updateFriendRequest(@PathVariable Long id, @RequestBody FriendRequest friendRequestDetails) {
        try {
            FriendRequest updatedFriendRequest = friendRequestService.updateFriendRequest(id, friendRequestDetails);
            return ResponseEntity.ok(updatedFriendRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFriendRequest(@PathVariable Long id) {
        friendRequestService.deleteFriendRequest(id);
        return ResponseEntity.noContent().build();
    }
}