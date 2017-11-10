package org.bracelet.controller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.bracelet.common.enums.ResponseCode;
import org.bracelet.common.model.Result;
import org.bracelet.common.session.SessionContext;
import org.bracelet.common.utils.OutputUtils;
import org.bracelet.entity.Message;
import org.bracelet.entity.User;
import org.bracelet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    private UserService userService;

    private Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/no_login")
    public void noLogin(@RequestBody String jsonString, HttpServletResponse response) throws IOException {
        logger.info("no login: " + jsonString);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("resCode", ResponseCode.NO_LOGIN);
        jsonObject.put("resMsg", "尚未登录");
        OutputUtils.print(response, jsonObject.toString());
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public void register(@RequestBody String jsonString, HttpServletResponse response) throws IOException {
        logger.info("register: " + jsonString);
        User user = new User(jsonString);
        Result result = userService.register(user);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("resCode", result.isSuccessful() ? ResponseCode.SUCCESSFUL : ResponseCode.ERROR);
        jsonObject.put("resMsg", result.getMessage());
        jsonObject.put("user", JSONObject.fromString(result.get("user").toString()));
//        logger.info(jsonObject.toString());
        OutputUtils.print(response, jsonObject.toString());
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(@RequestBody String jsonString, HttpServletResponse response, HttpSession session) throws IOException {
        logger.info("login: " + jsonString);
        JSONObject params = JSONObject.fromString(jsonString);
        Result result = userService.login(params.getString("phone"), params.getString("password"), session);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("resCode", result.isSuccessful() ? ResponseCode.SUCCESSFUL : ResponseCode.ERROR);
        jsonObject.put("resMsg", result.getMessage());
        jsonObject.put("user", result.isSuccessful() ? JSONObject.fromString(result.get("user").toString()) : new JSONObject());
        OutputUtils.print(response, jsonObject.toString());
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public void resetPassword(@RequestBody String jsonString, HttpServletResponse response) throws IOException {
        logger.info("reset password: " + jsonString);
        JSONObject params = JSONObject.fromString(jsonString);
        Result result = userService.modifyPassword(params.getString("phone"), params.getString("password"));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("resCode", result.isSuccessful() ? ResponseCode.SUCCESSFUL : ResponseCode.ERROR);
        jsonObject.put("resMsg", result.getMessage());
        jsonObject.put("user", result.isSuccessful() ? JSONObject.fromString(result.get("user").toString()) : new JSONObject());
        OutputUtils.print(response, jsonObject.toString());
    }

    @RequestMapping(value = "/modifyUserInformation", method = RequestMethod.POST)
    public void modifyUserInformation(@RequestBody String jsonString, HttpServletResponse response) throws IOException {
        logger.info("modify user information: " + jsonString);
        User user = new User(jsonString);
        user.setPassword(userService.getUser(user.getId()).getPassword());
        Result result = userService.modifyUserInformation(user);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("resCode", result.isSuccessful() ? ResponseCode.SUCCESSFUL : ResponseCode.ERROR);
        jsonObject.put("resMsg", result.getMessage());
        jsonObject.put("user", JSONObject.fromString(result.get("user").toString()));
        OutputUtils.print(response, jsonObject.toString());
    }

    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
    public void sendMessage(@RequestBody String jsonString, HttpServletResponse response) throws IOException {
        logger.info("send message: " + jsonString);
        JSONObject params = JSONObject.fromString(jsonString);
        Result result = userService.sendMessage(params.getString("content"), params.getLong("fromUserId"),
                params.getLong("toUserId"), params.getLong("time"));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("resCode", result.isSuccessful() ? ResponseCode.SUCCESSFUL : ResponseCode.ERROR);
        jsonObject.put("resMsg", result.getMessage());
        OutputUtils.print(response, jsonObject.toString());
    }

    @RequestMapping(value = "/addFriend", method = RequestMethod.POST)
    public void addFriend(@RequestBody String jsonString, HttpServletResponse response) throws IOException {
        logger.info("add friend " + jsonString);
        JSONObject params = JSONObject.fromString(jsonString);
        Result result = userService.sendMessage(params.getString("content"), params.getLong("fromUserId"),
                params.getLong("toUserId"), params.getLong("time"));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("resCode", result.isSuccessful() ? ResponseCode.SUCCESSFUL : ResponseCode.ERROR);
        jsonObject.put("resMsg", result.getMessage());
        jsonObject.put("user", result.isSuccessful() ? JSONObject.fromString(result.get("user").toString()) : new JSONObject());
        OutputUtils.print(response, jsonObject.toString());
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public void logout(@RequestBody String jsonString, HttpServletResponse response, HttpSession session) throws IOException {
        logger.info("logout: " + jsonString);
        JSONObject params = JSONObject.fromString(jsonString);
        userService.logout(params.getLong("userId"), session);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("resCode", ResponseCode.SUCCESSFUL);
        jsonObject.put("resMsg", "注销成功");
        OutputUtils.print(response, jsonObject.toString());
    }

    @RequestMapping(value = "/refreshMessages", method = RequestMethod.POST)
    public void refreshMessages(HttpServletResponse response, @SessionAttribute(SessionContext.ATTR_USER_ID) long userId) throws IOException {
        logger.info("refreshMessages: " + userId);
        List<Message> messagesList = userService.getUser(userId).getMessages();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("resCode", ResponseCode.SUCCESSFUL);
        jsonObject.put("resMsg", "刷新成功");
        JSONArray messages = new JSONArray();
        for (Message message : messagesList) {
            messages.put(JSONObject.fromString(message.toString()));
        }
        jsonObject.put("messages", messages);
        OutputUtils.print(response, jsonObject.toString());
    }
}
