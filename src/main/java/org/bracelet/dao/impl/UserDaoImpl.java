package org.bracelet.dao.impl;

import org.bracelet.dao.UserDao;
import org.bracelet.entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class UserDaoImpl extends DomainDaoImpl<User, Long> implements UserDao {

    @Override
    public User findUserByPhone(String phone) {
        Session session;
        Transaction transaction = null;
        User user = null;
        try {
            session = getCurrentSession();
            transaction = session.beginTransaction();
            user = (User)session.createQuery(" from User u where u.phone = :phone ")
                    .setParameter("phone", phone).uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return user;
    }

    @Override
    public long count() {
        Session session;
        Transaction transaction = null;
        long count = 0L;
        try {
            session = getCurrentSession();
            transaction = session.beginTransaction();
            count = ((Number)session.createQuery(" select count(u) from User u ").uniqueResult()).longValue();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return count;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> findAll() {
        Session session;
        Transaction transaction = null;
        List<User> users;
        try {
            session = getCurrentSession();
            transaction = session.beginTransaction();
            users = session.createQuery(" from User ").list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            users = new ArrayList<>();
        }
        return users;
    }

    @Override
    public void delete(Long id) {
        // do nothing
    }

    @Override
    public void delete(User entity) {
        // do nothing
    }
}
