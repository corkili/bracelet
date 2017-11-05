package org.bracelet.service.impl;

import org.bracelet.common.utils.Calculator;
import org.bracelet.crawler.Crawler;
import org.bracelet.dao.FoodDao;
import org.bracelet.dao.StateDao;
import org.bracelet.entity.*;
import org.bracelet.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class FoodServiceImpl implements FoodService {

    private FoodDao foodDao;

    private StateDao stateDao;

    private Map<Long, Map<Long, Food>> allFoods;

    public FoodServiceImpl() {
        allFoods = new HashMap<>();
    }



    @Override
    public void updateFood() {
        try {
            List<Food> foods = Crawler.craw();
            foodDao.batchSave(foods);
        } catch (IOException e) {
            System.err.println("error during updating foods.");
        }
    }



    @Override
    public Recipe makeRecipeForUser(User user) {
        Map<Long, Food> foods = new HashMap<>();
        for (FoodType foodType : user.getLikeFoods()) {
            foods.putAll(allFoods.get(foodType.getId()));
        }
        List<SportState> sportStates = stateDao.findStates(user, new Date(), new Date(), "sport");
        int bmr = Calculator.calBMR(user.getSex().equals("男") ? Calculator.MALE : Calculator.FEMALE,
                user.getWeight(), user.getHeight(), user.getAge());
        return null;
    }

    private void loadFoods() {
        List<Food> foods = foodDao.findFoods(false, new ArrayList<>());
        for (Food food : foods) {
            Long foodTypeId = food.getFoodType().getId();
            if (!allFoods.containsKey(foodTypeId))
                allFoods.put(foodTypeId, new HashMap<>());
            allFoods.get(foodTypeId).put(food.getId(), food);
        }
    }

    @Autowired
    public void setStateDao(StateDao stateDao) {
        this.stateDao = stateDao;
    }

    @Autowired
    public void setFoodDao(FoodDao foodDao) {
        this.foodDao = foodDao;
        loadFoods();
    }
}
