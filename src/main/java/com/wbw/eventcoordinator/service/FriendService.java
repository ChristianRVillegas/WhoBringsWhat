package com.wbw.eventcoordinator.service;

import com.wbw.eventcoordinator.entity.User;
import com.wbw.eventcoordinator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FriendService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getFriends(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return user.get().getFriends();
        } else {
            throw new RuntimeException("User not found with id " + userId);
        }
    }

    public void addFriend(Long userId, Long friendId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<User> friend = userRepository.findById(friendId);

        if (user.isPresent() && friend.isPresent()) {
            user.get().getFriends().add(friend.get());
            userRepository.save(user.get());
        } else {
            throw new RuntimeException("User or Friend not found");
        }
    }

    public void removeFriend(Long userId, Long friendId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<User> friend = userRepository.findById(friendId);

        if (user.isPresent() && friend.isPresent()) {
            user.get().getFriends().remove(friend.get());
            userRepository.save(user.get());
        } else {
            throw new RuntimeException("User or Friend not found");
        }
    }
}