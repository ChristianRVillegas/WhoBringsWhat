package com.wbw.eventcoordinator.entity;

import java.io.Serializable;
import java.util.Objects;

public class FriendId implements Serializable {
    private Long user;
    private Long friend;

    public FriendId() {}

    public FriendId(Long user, Long friend) {
        this.user = user;
        this.friend = friend;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendId friendId = (FriendId) o;
        return user.equals(friendId.user) && friend.equals(friendId.friend);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, friend);
    }
}
