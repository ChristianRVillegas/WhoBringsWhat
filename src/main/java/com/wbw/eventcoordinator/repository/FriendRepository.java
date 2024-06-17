package com.wbw.eventcoordinator.repository;

import com.wbw.eventcoordinator.entity.Friend;
import com.wbw.eventcoordinator.entity.FriendId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository extends JpaRepository<Friend, FriendId> {
}
