package org.bracelet.controller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.bracelet.common.enums.ResponseCode;
import org.bracelet.common.session.SessionContext;
import org.bracelet.common.utils.OutputUtils;
import org.bracelet.entity.HeartState;
import org.bracelet.entity.SleepState;
import org.bracelet.entity.SportState;
import org.bracelet.service.StateService;
import org.bracelet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/state")
public class StateController {
    private Logger logger = Logger.getLogger(StateController.class);

    private StateService stateService;

    private UserService userService;

    @Autowired
    public void setStateService(StateService stateService) {
        this.stateService = stateService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/addStates", method = RequestMethod.POST)
    public void addStates(@RequestBody String jsonString, HttpServletResponse response,
                          @SessionAttribute(SessionContext.ATTR_USER_ID) long userId) throws IOException {
        logger.info("add states: " + jsonString);
        JSONObject jsonObject = new JSONObject();
        stateService.addStates(userService.getUser(userId), jsonString);
        jsonObject.put("resCode", stateService.addStates(userService.getUser(userId), jsonString) ?
                ResponseCode.SUCCESSFUL : ResponseCode.ERROR);
        jsonObject.put("resMsg", "成功添加状态");
        OutputUtils.print(response, jsonObject.toString());
    }

    @RequestMapping(value = "/getStates", method = RequestMethod.POST)
    public void getStates(@RequestBody String jsonString, HttpServletResponse response,
                          @SessionAttribute(SessionContext.ATTR_USER_ID) long userId) throws IOException {
        logger.info("get states: " + jsonString);
        JSONObject params = JSONObject.fromString(jsonString);
        String status = params.getString("status");
        JSONObject jsonObject = new JSONObject();
        JSONArray stateArray = new JSONArray();
        boolean successful = true;
        if ("sport".equals(status)) {
            List<SportState> states = stateService.getStates(userService.getUser(userId), new Date(params.getLong("startTime")),
                    new Date(params.getLong("endTime")), status);
            for (SportState state : states) {
                stateArray.put(JSONObject.fromString(state.toString()));
            }
        } else if ("sleep".equals(status)) {
            List<SleepState> states = stateService.getStates(userService.getUser(userId), new Date(params.getLong("startTime")),
                    new Date(params.getLong("endTime")), status);
            for (SleepState state : states) {
                stateArray.put(JSONObject.fromString(state.toString()));
            }
        } else if ("heart".equals(status)) {
            List<HeartState> states = stateService.getStates(userService.getUser(userId), new Date(params.getLong("startTime")),
                    new Date(params.getLong("endTime")), status);
            for (HeartState state : states) {
                stateArray.put(JSONObject.fromString(state.toString()));
            }
        } else {
            successful = false;
        }
        jsonObject.put("resCode", successful ? ResponseCode.SUCCESSFUL : ResponseCode.ERROR);
        jsonObject.put("resMsg", successful ? "成功获取状态": "未知的状态类型");
        jsonObject.put("states", stateArray);
        OutputUtils.print(response, jsonObject.toString());
    }
}
