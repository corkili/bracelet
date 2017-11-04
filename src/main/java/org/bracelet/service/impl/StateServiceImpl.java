package org.bracelet.service.impl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.bracelet.dao.StateDao;
import org.bracelet.entity.*;
import org.bracelet.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
@Transactional
public class StateServiceImpl implements StateService {

    private StateDao stateDao;

    @Override
    public boolean addStates(User user, List<State> states) {
        for (State state : states) {
            state.setUser(user);
        }
        return stateDao.batchSave(states).size() == 0;
    }

    @Override
    public boolean addStates(User user, String jsonString) {
        JSONArray states = JSONArray.fromString(jsonString);
        List<State> stateList = new ArrayList<>();
        for (Iterator it = states.iterator(); it.hasNext(); ) {
            JSONObject state = (JSONObject) it.next();
            String status = state.getString("status");
            if ("sleep".equals(status)) {
                stateList.add(new SleepState(state.toString(), user));
            } else if ("sport".equals(status)) {
                stateList.add(new SportState(state.toString(), user));
            } else if ("heart".equals(status)) {
                stateList.add(new HeartState(state.toString(), user));
            } else {
                stateList.add(new State(state.toString(), user));
            }
        }
        return addStates(user, stateList);
    }

    @Override
    public <E extends State> List<E> getStates(User user, Date startTime, Date endTime, String status) {
        return stateDao.findStates(user, startTime, endTime, status);
    }

    @Autowired
    public void setStateDao(StateDao stateDao) {
        this.stateDao = stateDao;
    }
}
