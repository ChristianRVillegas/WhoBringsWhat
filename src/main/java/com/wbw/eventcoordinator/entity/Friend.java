package com.wbw.eventcoordinator.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(FriendId.class)
public class Friend implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "friend_id", nullable = false)
    private User friend;

    // Constructors, getters, and setters

    public Friend() {}

    public Friend(User user, User friend) {
        this.user = user;
        this.friend = friend;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }
}
