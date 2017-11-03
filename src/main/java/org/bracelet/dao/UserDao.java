package org.bracelet.dao;

import org.bracelet.entity.User;

public interface UserDao extends DomainDao<User, Long> {
    User findUserByPhone(String phone);

    long count();
}
