package org.example;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;

@Embeddable
public class GroupMember {

    @ManyToOne
    private Group group;

    @ManyToOne
    private User user;

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "GroupMember{" +
                "group=" + group.getName() +
                ", user=" + user.getUsername() +
                '}';
    }
}
