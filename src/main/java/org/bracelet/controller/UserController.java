package org.bracelet.controller;

import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.bracelet.common.enums.ResponseCode;
import org.bracelet.common.model.Result;
import org.bracelet.entity.User;
import org.bracelet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    private UserService userService;

    private Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public void register(@RequestBody String jsonString, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        logger.info("register: " + jsonString);
        User user = new User(jsonString);
        Result result = userService.register(user);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("resCode", result.isSuccessful() ? ResponseCode.SUCCESSFUL : ResponseCode.ERROR);
        jsonObject.put("resMsg", result.getMessage());
        jsonObject.put("user", JSONObject.fromString(result.get("user").toString()));
        logger.info(jsonObject.toString());
        writer.println(jsonObject.toString());
        writer.flush();
        writer.close();
    }


}
