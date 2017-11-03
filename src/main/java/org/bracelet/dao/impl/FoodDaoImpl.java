package org.bracelet.dao.impl;

import org.bracelet.dao.FoodDao;
import org.bracelet.entity.Food;
import org.bracelet.entity.FoodType;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class FoodDaoImpl extends DomainDaoImpl<Food, Long> implements FoodDao{
    @Override
    @SuppressWarnings("unchecked")
    public List<Food> findFoods(boolean fuzzy, List<FoodType> foodTypes, String... params) throws IllegalArgumentException {
        Session session = getCurrentSession();
        if (params.length % 2 == 1) {
            throw new IllegalArgumentException("The number of params should be even");
        }
        String hql = " select f from Food f ";
        StringBuilder where = new StringBuilder();
        for (int j = 0; j < params.length; j += 2) {
            if (where.length() != 0) {
                where.append(" and ");
            }
            where.append(" f.").append(params[j]);
            if (fuzzy) {
                where.append(" like :").append(params[j]);
            } else {
                where.append(" = :").append(params[j]);
            }
        }

        if (where.length() != 0 && foodTypes.size() != 0) {
            where.append(" and (");
        }
        for (int i = 0; i < foodTypes.size(); i++) {
            where.append("f.foodType = :foodType").append(i);
            if (i != foodTypes.size() - 1) {
                where.append(" or ");
            }
        }
        if (foodTypes.size() != 0) {
            where.append(")");
        }

        if (where.length() != 0) {
            hql += " where " + where;
        }
        Query query = session.createQuery(hql);
        for (int j = 0; j < params.length; j += 2) {
            query.setParameter(params[j], params[j + 1]);
        }
        for (int i = 0; i < foodTypes.size(); i++) {
            query.setParameter("foodType" + i, foodTypes.get(i));
        }
        try {
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public long count() {
        Session session;
        Transaction transaction = null;
        long count = 0L;
        try {
            session = getCurrentSession();
            transaction = session.beginTransaction();
            count = ((Number)session.createQuery(" select count(f) from Food f ").uniqueResult()).longValue();
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
    public List<Food> findAll() {
        Session session;
        Transaction transaction = null;
        List<Food> foods;
        try {
            session = getCurrentSession();
            transaction = session.beginTransaction();
            foods = session.createQuery(" from Food ").list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            foods = new ArrayList<>();
        }
        return foods;
    }
}
