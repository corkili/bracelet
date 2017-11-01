package org.bracelet.repository.impl;

import org.bracelet.entity.Person;
import org.bracelet.repository.PersonRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 李浩然 on 2017/8/7.
 */
@Repository
public class PersonRepositoryImpl implements PersonRepository {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return this.sessionFactory.openSession();
    }

    public Person load(Long id) {
        return (Person)getCurrentSession().load(Person.class, id);
    }

    public Person get(Long id) {
        return (Person)getCurrentSession().get(Person.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<Person> findAll() {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        List<Person> personList = session.createQuery(" from Person ").list();
        transaction.commit();
        return personList;
    }

    public void persist(Person entity) {
        getCurrentSession().persist(entity);
    }

    public Long save(Person entity) {
        return (Long)getCurrentSession().save(entity);
    }

    public void saveOrUpdate(Person entity) {
        Session session = getCurrentSession();
        session.beginTransaction();
        session.saveOrUpdate(entity);
        session.getTransaction().commit();
        session.close();
    }

    public void delete(Long id) {
        Person person = load(id);
        getCurrentSession().delete(person);
    }

    public void flush() {
        getCurrentSession().flush();
    }
}
