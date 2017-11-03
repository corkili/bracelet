package org.bracelet.dao;

import org.bracelet.entity.State;
import org.bracelet.entity.User;

import java.util.Date;
import java.util.List;

public interface StateDao extends DomainDao<State, Long> {
    <E extends State> List<E> findStates(User user, Date startTime, Date endTime, String status);
}
