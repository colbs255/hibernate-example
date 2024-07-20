package org.example;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        var user = new User();
        user.setFirst("Peter");
        user.setLast("Parker");
        user.setUsername("pparker");

        final StandardServiceRegistry registry =
                new StandardServiceRegistryBuilder()
                        .build();
        var sessionFactory =
                new MetadataSources(registry)
                        .addAnnotatedClass(User.class)
                        .buildMetadata()
                        .buildSessionFactory();

        sessionFactory.inTransaction(session -> {
            session.persist(user);
        });

        sessionFactory.inTransaction(session -> {
            session.createSelectionQuery("from User", User.class)
                    .getResultStream()
                    .forEach(System.out::println);
        });
    }
}