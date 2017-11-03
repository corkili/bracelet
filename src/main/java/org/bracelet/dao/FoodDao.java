package org.bracelet.dao;


import org.bracelet.entity.Food;
import org.bracelet.entity.FoodType;

import java.util.List;

public interface FoodDao extends DomainDao<Food, Long> {
    List<Food> findFoods(boolean fuzzy, List<FoodType> foodTypes, String... params) throws IllegalArgumentException;

    long count();
}

