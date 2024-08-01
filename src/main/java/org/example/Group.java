package org.example;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "db_group")
public class Group {

    @Id
    @GeneratedValue
    private long id;

    private String name;

    @ManyToMany
    List<User> owners;

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

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", users=" + owners +
                '}';
    }
}
