package org.bracelet.entity;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Recipe {
    private List<Food> foods;

    public Recipe() {
        foods = new ArrayList<>();
    }

    public Recipe(String jsonString) {
        foods = new ArrayList<>();
        JSONObject json = JSONObject.fromString(jsonString);
        JSONArray foodArray = json.getJSONArray("foods");
        for (Iterator it = foodArray.iterator(); it.hasNext(); ) {
            foods.add(new Food(((JSONObject)it.next()).toString()));
        }
    }

    public void addFood(Food food) {
        foods.add(food);
    }

    public void delFood(Food food) {
        foods.remove(food);
    }

    @Override
    public String toString() {
        JSONObject json = new JSONObject();
        JSONArray foodArray = new JSONArray();
        for (Food food : foods) {
            foodArray.put(food.toString());
        }
        json.put("foods", foodArray);
        json.put("size", foods.size());
        return json.toString();
    }

}
