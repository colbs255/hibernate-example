package org.example;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.SQLDelete;

import java.util.List;

@Entity
@Table(name = "db_group")
@SQLDelete(sql = "UPDATE db_group SET deleted = true where id=?")
public class Group {

    @Id
    @GeneratedValue
    private long id;

    private String name;

    @ManyToMany
    List<User> owners;

    @ElementCollection(fetch = FetchType.LAZY)
            @CollectionTable(
                    joinColumns = @JoinColumn(name = "GroupId")
            )
    List<GroupMember> members;

    boolean deleted;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getOwners() {
        return owners;
    }

    public void setOwners(List<User> owners) {
        this.owners = owners;
    }

    public List<GroupMember> getMembers() {
        return members;
    }

    public void setMembers(List<GroupMember> members) {
        this.members = members;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", owners=" + owners +
                ", members=" + members +
                ", deleted=" + deleted +
                '}';
    }
}
