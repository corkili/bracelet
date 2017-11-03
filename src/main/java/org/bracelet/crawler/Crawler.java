package org.bracelet.crawler;

import org.bracelet.dao.FoodDao;
import org.bracelet.dao.impl.FoodDaoImpl;
import org.bracelet.entity.Food;
import org.bracelet.entity.FoodType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class Crawler {

    private Map<FoodType, List<Food>> foodTypeListMap;

    private Crawler() {
        foodTypeListMap = new HashMap<>();
    }

    private void craw() throws IOException {
        String baseUrl = "http://yingyang.118cha.com/";
        Document document = Jsoup.connect(baseUrl)
                .userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)")
                .get();
        Elements listAElements = document.select("#mainbox").select("div.leftbox")
                .select("div:nth-child(2)").select("div.mcon").select("p:nth-child(3)").select("a");
        Map<String, String> listMap = new HashMap<>();

        for (Element listAElement : listAElements) {
            listMap.put(listAElement.text(), baseUrl + listAElement.attr("href"));
        }

        // 爬取每一种食物的具体信息
        for (Map.Entry<String, String> entry : listMap.entrySet()) {
//            System.out.println(entry.getKey() + " " + entry.getValue());

            FoodType foodType = new FoodType();
            foodType.setName(entry.getKey());

            // 循环爬取每一个列表的每一种食物的链接
            document = Jsoup.connect(entry.getValue())
                    .userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)").get();

            Elements foodAElements = document.select("#mainbox").select("div.leftbox")
                    .select("div:nth-child(2)").select("div.mcon").select("ul").select("li");

            List<Food> foods = new ArrayList<>();

            for (Element foodAElement : foodAElements) {
                String foodName = foodAElement.select("a").text();
                String foodUrl = baseUrl + foodAElement.select("a").attr("href");
                // 一种食物的具体信息
                document = Jsoup.connect(foodUrl)
                        .userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)")
                        .get();

                Elements foodElements = document.select("#mainbox > div.leftbox > div:nth-child(2) > div.mcon > table > tbody > tr > td");

                Food food = new Food();

                food.setName(foodName);
                food.setFoodType(foodType);

                int i = 0;

                for (Element foodElement : foodElements) {
                    String valueStr = foodElement.text();
                    Scanner scanner = new Scanner(valueStr);
                    double value = scanner.nextDouble();
                    switch (i) {
                        case 0: food.setHeatContent(value);break;
                        case 1: food.setThiamine(value);break;
                        case 2: food.setCalcium(value);break;
                        case 3: food.setProtein(value);break;
                        case 4: food.setRiboflavin(value);break;
                        case 5: food.setMagnesium(value);break;
                        case 6: food.setFat(value);break;
                        case 7: food.setNiacin(value);break;
                        case 8: food.setIron(value);break;
                        case 9: food.setCarbohydrate(value);break;
                        case 10: food.setVitaminC(value);break;
                        case 11: food.setManganese(value);break;
                        case 12: food.setDietaryFibre(value);break;
                        case 13: food.setVitaminE(value);break;
                        case 14: food.setZinc(value);break;
                        case 15: food.setVitaminA(value);break;
                        case 16: food.setCholesterol(value);break;
                        case 17: food.setCopper(value);break;
                        case 18: food.setCarotene(value);break;
                        case 19: food.setPotassium(value);break;
                        case 20: food.setPhosphorus(value);break;
                        case 21: food.setRetinolEquivalent(value);break;
                        case 22: food.setSodium(value);break;
                        case 23: food.setSelenium(value);break;
                        default:break;
                    }
                    i++;
                }
                foods.add(food);
            }

            foodTypeListMap.put(foodType, foods);
        }

        int count = 0, i = 1;
        for (Map.Entry<FoodType, List<Food>> entry : foodTypeListMap.entrySet()) {
            count += entry.getValue().size();
            System.out.println(i + ":\t" + entry.getKey() + " (count: " + entry.getValue().size() + ")");
            for (Food food : entry.getValue()) {
                System.out.println(food);
            }
            i++;
        }
        System.out.println("Total foods: " + count);

    }

    public static void main(String[] args) throws IOException {
        Crawler crawler = new Crawler();
        crawler.craw();
        List<Food> foods = new ArrayList<>();
        for (List<Food> foodList : crawler.foodTypeListMap.values()) {
            foods.addAll(foodList);
        }
        System.out.println("done!");
    }
}
