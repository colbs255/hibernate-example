package org.example;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws SQLException {
        var user = new User();
        user.setFirst("Peter");
        user.setLast("Parker");
        user.setUsername("pparker");

        var group = new Group();
        group.setName("test_group");
        group.setOwners(List.of(user));

        final StandardServiceRegistry registry =
                new StandardServiceRegistryBuilder()
                        .build();
        var sessionFactory =
                new MetadataSources(registry)
                        .addAnnotatedClass(User.class)
                        .addAnnotatedClass(Group.class)
                        .buildMetadata()
                        .buildSessionFactory();

        sessionFactory.inTransaction(session -> {
            session.persist(user);
            session.persist(group);
        });

        sessionFactory.inTransaction(session -> {
            session.remove(group);
        });

        sessionFactory.inTransaction(session -> {
            session.createSelectionQuery("from User", User.class)
                    .getResultStream()
                    .forEach(System.out::println);

            session.createSelectionQuery("from Group", Group.class)
                    .getResultStream()
                    .forEach(System.out::println);
        });
    }
}