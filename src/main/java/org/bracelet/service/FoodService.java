package org.bracelet.service;

import org.bracelet.entity.Recipe;
import org.bracelet.entity.User;


public interface FoodService {
    void updateFood();

    Recipe makeRecipeForUser(User user);
}
