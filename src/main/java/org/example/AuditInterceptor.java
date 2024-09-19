package org.example;

import org.hibernate.*;
import org.hibernate.type.Type;

import java.util.Arrays;

public class AuditInterceptor implements Interceptor {
    private final SessionFactory sessionFactory;
    private final String author;

    public AuditInterceptor(SessionFactory sessionFactory, String author) {
        this.sessionFactory = sessionFactory;
        this.author = author;
    }

    @Override
    public boolean onFlushDirty(Object entity, Object id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) throws CallbackException {
        sessionFactory.inTransaction(s -> {
            var event = new AuditEvent();
            event.setAuthor(author);
            event.setThing("old: " + Arrays.toString(previousState) + "new: " + Arrays.toString(currentState));
            s.persist(event);
        });
        return true;
    }
}
