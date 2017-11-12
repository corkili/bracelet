package org.bracelet.dao.impl;

import org.bracelet.dao.StateDao;
import org.bracelet.entity.State;
import org.bracelet.entity.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public class StateDaoImpl extends DomainDaoImpl<State, Long> implements StateDao {

    @Override
    @SuppressWarnings("unchecked")
    public <E extends State> List<E> findStates(User user, Date startTime, Date endTime, String status) {
        Session session;
        Transaction transaction = null;
        List<State> states;
        try {
            session = getCurrentSession();
            transaction = session.beginTransaction();
            states = session.createQuery(" from State s where s.user = :user and s.status = :status and " +
                    " ((s.startTime between :startTime and :endTime) or (s.endTime between :startTime and :endTime)) ")
                    .setParameter("user", user).setParameter("status", status)
                    .setParameter("startTime", startTime).setParameter("endTime", endTime)
                    .list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            states = new ArrayList<>();
        }
        List<E> result = new ArrayList<>();
        for (State state : states) {
            result.add((E) state);
        }
        return result;
    }
}
