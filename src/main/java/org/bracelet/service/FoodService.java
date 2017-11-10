package org.bracelet.service;

import org.bracelet.entity.FoodType;
import org.bracelet.entity.Recipe;
import org.bracelet.entity.User;

import java.util.List;


public interface FoodService {
    void updateFood();

    Recipe makeRecipeForUser(User user);

    List<FoodType> getAllFoodTypes();
}
