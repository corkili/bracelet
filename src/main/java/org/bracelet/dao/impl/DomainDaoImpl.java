package org.bracelet.dao.impl;

import org.bracelet.dao.DomainDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public abstract class DomainDaoImpl<T, PK extends Serializable> implements DomainDao<T, PK> {

    private Class<T> entityClass;

    private SessionFactory sessionFactory;

    final Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    public DomainDaoImpl() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        entityClass = (Class) params[0];
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(PK id) {
        Session session;
        Transaction transaction = null;
        T t = null;
        try {
            session = getCurrentSession();
            transaction = session.beginTransaction();
            t = (T)session.get(entityClass, id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return t;
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>();
    }

    @Override
    public void persist(T entity) {
        Session session;
        Transaction transaction = null;
        try {
            session = getCurrentSession();
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public PK save(T entity) {
        Session session;
        Transaction transaction = null;
        PK pk = null;
        try {
            session = getCurrentSession();
            transaction = session.beginTransaction();
            pk = (PK)session.save(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return pk;
    }

    @Override
    public void saveOrUpdate(T entity) {
        Session session;
        Transaction transaction = null;
        try {
            session = getCurrentSession();
            transaction = session.beginTransaction();
            session.saveOrUpdate(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void delete(PK id) {
        Session session;
        Transaction transaction = null;
        T t;
        try {
            session = getCurrentSession();
            transaction = session.beginTransaction();
            t = (T)session.get(entityClass, id);
            if (t != null) {
                session.delete(t);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void delete(T entity) {
        Session session;
        Transaction transaction = null;
        try {
            session = getCurrentSession();
            transaction = session.beginTransaction();
            session.delete(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public List<T> batchSave(List<T> entities) {
        List<T> errorEntities = new ArrayList<>();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            for (int i = 0; i < entities.size(); i++) {
                if (session.save(entities.get(i)) == null) {
                    errorEntities.add(entities.get(i));
                }
                if (i % 10 == 0) {
                    session.flush();
                    session.clear();
                }
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            session.close();
        }
        entities.removeAll(errorEntities);
        return errorEntities;
    }

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
