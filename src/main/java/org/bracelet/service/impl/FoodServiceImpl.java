package org.bracelet.service.impl;

import org.bracelet.common.utils.FoodHelper;
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
        List<Food> foods = new ArrayList<>();
        for (FoodType foodType : user.getLikeFoods()) {
            foods.addAll(allFoods.get(foodType.getId()).values());
        }
        // loadStates
        List<List<SportState>> sportLists = new ArrayList<>();
        List<List<SleepState>> sleepLists = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date start = new Date();
        Date end = new Date();
        for (int i = 0; i < 7; i++) {
            end.setTime(calendar.getTimeInMillis());
            calendar.add(Calendar.DATE, -1);
            start.setTime(calendar.getTimeInMillis());
            sportLists.add(stateDao.findStates(user, start, end, "sport"));
            sleepLists.add(stateDao.findStates(user, start, end, "sleep"));
        }
        List<Integer> caloriesList = FoodHelper.calAllCalories(user.getWeight(), user.getHeight(), user.getAge(),
                user.getSex().equals("男") ? FoodHelper.MALE : FoodHelper.FEMALE, sportLists, sleepLists);
        int calories = FoodHelper.forecastCalories(user.getWeight(), user.getHeight(), user.getAge(),
                user.getSex().equals("男") ? FoodHelper.MALE : FoodHelper.FEMALE, caloriesList);
        calories = FoodHelper.calNeedCalories(calories);
        return FoodHelper.makeRecipe(foods, calories, user.getAge(), user.getSex().equals("男") ? FoodHelper.MALE : FoodHelper.FEMALE);
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
