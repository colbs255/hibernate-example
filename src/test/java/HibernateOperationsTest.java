import org.example.AuditInterceptor;
import org.example.Group;
import org.example.GroupMember;
import org.example.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.envers.AuditReaderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class HibernateOperationsTest {

    private SessionFactory sessionFactory;
    private static final String username = "pparker";
    private long id;

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
        var user = new User();
        user.setFirst("Peter");
        user.setLast("Parker");
        user.setUsername(username);
        user.setAge(2);
        sessionFactory.inTransaction(session -> {
            session.persist(user);
        });
        id = user.getId();
    }

    @Test
    void nested_session() {
        sessionFactory.inTransaction(session -> {
            var storedUser = getUser(session);
            storedUser.setLast("another");
            var another = getUser(session);
            sessionFactory.inTransaction(s -> {
                var again = getUser(s);
                assertThat(storedUser.getLast()).isEqualTo("another");
                assertThat(another.getLast()).isEqualTo("another");
                assertThat(again.getLast()).isEqualTo("Parker");
            });
        });
    }

    @Test
    void audit() {
        sessionFactory.inTransaction(session -> {
            var auditReader = AuditReaderFactory.get(session);
            var storedUser = getUser(session);
            var all = auditReader.getRevisions(User.class, id).stream().map(v -> auditReader.find(User.class, id, v)).toList();
            storedUser.setLast("another");
            session.persist(storedUser);
            session.flush();
            var all2 = auditReader.getRevisions(User.class, id).stream().map(v -> auditReader.find(User.class, id, v)).toList();
            assertThat(all).isEqualTo(all2);
            assertThat(storedUser.getLast()).isEqualTo("another");
            assertThat(all.getLast().getLast()).isEqualTo("Parker");
            assertThat(all2.getLast().getLast()).isEqualTo("Parker");
        });

        sessionFactory.inTransaction(session -> {
            var auditReader = AuditReaderFactory.get(session);
            var all = auditReader.getRevisions(User.class, id).stream().map(v -> auditReader.find(User.class, id, v)).toList();
            assertThat(all).hasSize(2);
        });
    }

    @Test
    void refresh() {
        sessionFactory.inTransaction(session -> {
            var storedUser = getUser(session);
            storedUser.setLast("another");
            assertThat(storedUser.getLast()).isEqualTo("another");
            session.refresh(storedUser);
            assertThat(storedUser.getLast()).isEqualTo("Parker");
        });
    }

    @Test
    void evict() {
        sessionFactory.inTransaction(session -> {
            var storedUser = getUser(session);
            storedUser.setLast("another");
            session.evict(storedUser);
            var another = getUser(session);
            assertThat(storedUser.getLast()).isEqualTo("another");
            assertThat(another.getLast()).isEqualTo("Parker");
        });
    }

    @Test
    void interceptor() {
        try (var session = sessionFactory.withOptions().interceptor(new AuditInterceptor(sessionFactory)).openSession()) {
            var txn = session.beginTransaction();
            var storedUser = getUser(session);
            storedUser.setLast("another");
            session.persist(storedUser);
            txn.commit();
        }
        System.out.println("here");

    }

    @Test
    void hibernate_audit_feature() {

    }

    User getUser(Session session) {
        var users = session.createSelectionQuery("from User", User.class).stream().toList();
        if (users.size() != 1) {
            throw new RuntimeException();
        }
        return users.getFirst();
    }
}