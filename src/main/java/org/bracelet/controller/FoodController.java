package org.bracelet.controller;

import org.bracelet.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class FoodController {

    private FoodService foodService;

    @RequestMapping(value = "/updateFoods", method = RequestMethod.GET)
    public void updateFoods() {
        foodService.updateFood();
    }

    @Autowired
    public void setFoodService(FoodService foodService) {
        this.foodService = foodService;
    }
}
