package com.wbw.eventcoordinator.service;

import com.wbw.eventcoordinator.entity.FriendRequest;
import com.wbw.eventcoordinator.repository.FriendRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FriendRequestService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    public FriendRequest createFriendRequest(FriendRequest friendRequest) {
        return friendRequestRepository.save(friendRequest);
    }

    public List<FriendRequest> getAllFriendRequests() {
        return friendRequestRepository.findAll();
    }

    public Optional<FriendRequest> getFriendRequestById(Long id) {
        return friendRequestRepository.findById(id);
    }

    public FriendRequest updateFriendRequest(Long id, FriendRequest friendRequestDetails) {
        return friendRequestRepository.findById(id).map(friendRequest -> {
            friendRequest.setStatus(friendRequestDetails.getStatus());
            return friendRequestRepository.save(friendRequest);
        }).orElseThrow(() -> new RuntimeException("FriendRequest not found with id " + id));
    }

    public void deleteFriendRequest(Long id) {
        friendRequestRepository.deleteById(id);
    }
}