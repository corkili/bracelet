package org.bracelet.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 李浩然 on 2017/8/7.
 */
public interface DomainDao<T, PK extends Serializable> {

    T get(PK id);

    List<T> findAll();

    void persist(T entity);

    PK save(T entity);

    void saveOrUpdate(T entity);

    void delete(PK id);

    void delete(T entity);

    List<T> batchSave(List<T> entities);
}
