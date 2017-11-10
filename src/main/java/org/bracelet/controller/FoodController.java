package org.bracelet.controller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.bracelet.common.enums.ResponseCode;
import org.bracelet.common.model.Result;
import org.bracelet.common.session.SessionContext;
import org.bracelet.common.utils.OutputUtils;
import org.bracelet.entity.FoodType;
import org.bracelet.service.FoodService;
import org.bracelet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping(value = "/food")
public class FoodController {

    private Logger logger = Logger.getLogger(FoodController.class);

    private FoodService foodService;

    private UserService userService;

    @RequestMapping(value = "/updateFoods", method = RequestMethod.GET)
    public void updateFoods() {
        foodService.updateFood();
    }

    @Autowired
    public void setFoodService(FoodService foodService) {
        this.foodService = foodService;
    }

    @RequestMapping(value = "/getAllFoodType", method = RequestMethod.POST)
    public void getAllFoodType(HttpServletResponse response) throws IOException {
        logger.info("get all food type");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("resCode", ResponseCode.SUCCESSFUL);
        jsonObject.put("resMsg", "获取成功");
        JSONArray foodTypes = new JSONArray();
        for (FoodType foodType : foodService.getAllFoodTypes()) {
            foodTypes.put(JSONObject.fromString(foodType.toString()));
        }
        jsonObject.put("foodTypes", foodTypes);
        OutputUtils.print(response, jsonObject.toString());
    }

    @RequestMapping(value = "/makeRecipe", method = RequestMethod.POST)
    public void makeRecipe(HttpServletResponse response, @SessionAttribute(SessionContext.ATTR_USER_ID) long userId) throws IOException {
        logger.info("make recipe");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("resCode", ResponseCode.SUCCESSFUL);
        jsonObject.put("resMsg", "成功获取食谱");
        jsonObject.put("recipe", JSONObject.fromString(foodService.makeRecipeForUser(
                userService.getUser(userId)).toString()));
        logger.info(jsonObject.toString());
        OutputUtils.print(response, jsonObject.toString());
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
