import org.example.Group;
import org.example.GroupMember;
import org.example.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class HibernateOperationsTest {

    private SessionFactory sessionFactory;

    @BeforeEach
    void setup() {
        final StandardServiceRegistry registry =
                new StandardServiceRegistryBuilder()
                        .build();
        sessionFactory =
                new MetadataSources(registry)
                        .addAnnotatedClass(User.class)
                        .addAnnotatedClass(Group.class)
                        .addAnnotatedClass(GroupMember.class)
                        .buildMetadata()
                        .buildSessionFactory();
    }

    @Test
    void persist_user() {
        var user = new User();
        user.setFirst("Peter");
        user.setLast("Parker");
        user.setUsername("pparker");

        sessionFactory.inTransaction(session -> {
            session.persist(user);
        });

        sessionFactory.inTransaction(session -> {
            var users = session.createSelectionQuery("from User", User.class).list();
            assertThat(users).containsExactly(user);
        });
    }

    @Test
    void persist_group_with_user() {
        var user = new User();
        user.setFirst("Peter");
        user.setLast("Parker");
        user.setUsername("pparker");

        var group = new Group();
        group.setName("test_group");
        group.setOwners(List.of(user));

        sessionFactory.inTransaction(session -> {
            session.persist(user);
            session.persist(group);
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

    @Test
    void delete_group() {
        var user = new User();
        user.setFirst("Peter");
        user.setLast("Parker");
        user.setUsername("pparker");

        var group = new Group();
        group.setName("test_group");
        group.setOwners(List.of(user));
        var member = new GroupMember();
        member.setGroup(group);
        member.setUser(user);
        group.setMembers(List.of(member));

        sessionFactory.inTransaction(session -> {
            session.persist(user);
            session.persist(group);
        });

        sessionFactory.inTransaction(session -> {
            session.createSelectionQuery("from Group", Group.class)
                    .getResultStream()
                    .forEach(System.out::println);
        });

        sessionFactory.inTransaction(session -> {
            session.remove(group);
        });

        sessionFactory.inTransaction(session -> {
            session.createSelectionQuery("from Group", Group.class)
                    .getResultStream()
                    .forEach(System.out::println);
        });
    }
}
