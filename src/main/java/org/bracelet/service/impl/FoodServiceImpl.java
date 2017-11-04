package org.bracelet.service.impl;

import org.bracelet.crawler.Crawler;
import org.bracelet.dao.FoodDao;
import org.bracelet.entity.Food;
import org.bracelet.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class FoodServiceImpl implements FoodService {

    private FoodDao foodDao;

    @Autowired
    public void setFoodDao(FoodDao foodDao) {
        this.foodDao = foodDao;
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
}
