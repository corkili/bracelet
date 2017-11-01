package org.bracelet.entity;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

/**
 * Created by 李浩然
 * On 2017/8/7.
 */
@Entity
@Table(name = "user")
public class User {

    /**
     * 用户ID
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 用户名
     */
    @Column(name = "username")
    private String username;

    /**
     * 用户口令
     */
    @Column(name = "password")
    private String password;

    /**
     * 姓名
     */
    @Column(name = "name")
    private String name;

    /**
     * 性别
     */
    @Column(name = "sex")
    private String sex;

    /**
     * 生日
     */
    @Column(name = "birthday")
    private Date birthday;

    /**
     * 年龄
     */
    @Column(name = "age")
    private Integer age;

    /**
     * 体重
     */
    @Column(name = "weight", scale = 2)
    private Double weight;

    /**
     * 身高
     */
    @Column(name = "height", scale = 2)
    private Double height;

    /**
     * 手机号
     */
    @Column(name = "phone")
    private String phone;

    @Transient
    private List<FoodType> likeFoods;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<FoodType> getLikeFoods() {
        return likeFoods;
    }

    public void setLikeFoods(List<FoodType> likeFoods) {
        this.likeFoods = likeFoods;
    }
}
