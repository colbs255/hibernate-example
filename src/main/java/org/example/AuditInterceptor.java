package org.example;

import org.hibernate.CallbackException;
import org.hibernate.Interceptor;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.type.Type;

import java.util.Iterator;

public class AuditInterceptor implements Interceptor {
    private final SessionFactory sessionFactory;

    public AuditInterceptor(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean onLoad(Object entity, Object id, Object[] state, String[] propertyNames, Type[] types) throws CallbackException {
        return Interceptor.super.onLoad(entity, id, state, propertyNames, types);
    }

    @Override
    public boolean onFlushDirty(Object entity, Object id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) throws CallbackException {
        return Interceptor.super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
    }

    @Override
    public boolean onSave(Object entity, Object id, Object[] state, String[] propertyNames, Type[] types) throws CallbackException {
        return Interceptor.super.onSave(entity, id, state, propertyNames, types);
    }

    @Override
    public void onDelete(Object entity, Object id, Object[] state, String[] propertyNames, Type[] types) throws CallbackException {
        Interceptor.super.onDelete(entity, id, state, propertyNames, types);
    }

    @Override
    public void onCollectionRecreate(Object collection, Object key) throws CallbackException {
        Interceptor.super.onCollectionRecreate(collection, key);
    }

    @Override
    public void onCollectionRemove(Object collection, Object key) throws CallbackException {
        Interceptor.super.onCollectionRemove(collection, key);
    }

    @Override
    public void onCollectionUpdate(Object collection, Object key) throws CallbackException {
        Interceptor.super.onCollectionUpdate(collection, key);
    }

    @Override
    public void preFlush(Iterator<Object> entities) throws CallbackException {
        Interceptor.super.preFlush(entities);
    }

    @Override
    public void postFlush(Iterator<Object> entities) throws CallbackException {
        Interceptor.super.postFlush(entities);
    }

    @Override
    public Boolean isTransient(Object entity) {
        return Interceptor.super.isTransient(entity);
    }

    @Override
    public int[] findDirty(Object entity, Object id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        return Interceptor.super.findDirty(entity, id, currentState, previousState, propertyNames, types);
    }

    @Override
    public void afterTransactionBegin(Transaction tx) {
        Interceptor.super.afterTransactionBegin(tx);
    }

    @Override
    public void beforeTransactionCompletion(Transaction tx) {
        Interceptor.super.beforeTransactionCompletion(tx);
    }

    @Override
    public void afterTransactionCompletion(Transaction tx) {
        Interceptor.super.afterTransactionCompletion(tx);
    }
}
