package org.bracelet.service;

import org.bracelet.entity.State;
import org.bracelet.entity.User;

import java.util.Date;
import java.util.List;

public interface StateService {
    boolean addStates(User user, List<State> states);

    boolean addStates(User user, String jsonString);

    <E extends State> List<E> getStates(User user, Date startTime, Date endTime, String status);
}
