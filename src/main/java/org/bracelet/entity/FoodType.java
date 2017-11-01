package org.bracelet.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "foodType")
public class FoodType {

    /**
     * 食物类型ID
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 食物类型名称
     */
    @Column(name = "name", unique = true, nullable = false, length = 20)
    private String name;

    /**
     * 食物列表
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "foodType")
    private List<Food> foods;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }

    @Override
    public String toString() {
        return "FoodType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", foods=" + foods +
                '}';
    }
}
