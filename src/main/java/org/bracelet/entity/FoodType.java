package org.bracelet.entity;

import javax.persistence.*;
import java.util.List;

/**
 * 食物类型
 */
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

    @Override
    public String toString() {
        return "FoodType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FoodType)) return false;

        FoodType foodType = (FoodType) o;

        if (id != null ? !id.equals(foodType.id) : foodType.id != null) return false;
        if (name != null ? !name.equals(foodType.name) : foodType.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
