package org.bracelet.entity;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户实体
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
    @Column(name = "username", length = 128, nullable = false)
    private String username;

    /**
     * 用户口令
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * 姓名
     */
    @Column(name = "name", length = 64)
    private String name;

    /**
     * 性别
     */
    @Column(name = "sex", length = 10)
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
    @Column(name = "phone", length = 20, unique = true, nullable = false)
    private String phone;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "registerTime")
    private java.util.Date registerTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lastLoginTime")
    private java.util.Date lastLoginTime;

    /**
     * 用户饮食偏好
     */
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_food",
            joinColumns = {@JoinColumn(name = "userId")},
            inverseJoinColumns = {@JoinColumn(name = "foodTypeId")})
    private List<FoodType> likeFoods;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "friends",
            joinColumns = {@JoinColumn(name = "userId")},
            inverseJoinColumns = {@JoinColumn(name = "friendId")})
    private List<User> friends;

    public User() {
        likeFoods = new ArrayList<FoodType>();
        friends = new ArrayList<User>();
        this.weight = 0.0;
        this.height = 0.0;
    }

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

    public java.util.Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(java.util.Date registerTime) {
        this.registerTime = registerTime;
    }

    public java.util.Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(java.util.Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday=" + birthday +
                ", age=" + age +
                ", weight=" + weight +
                ", height=" + height +
                ", phone='" + phone + '\'' +
                ", registerTime=" + registerTime +
                ", lastLoginTime=" + lastLoginTime +
                ", likeFoods=" + likeFoods +
                ", friends=" + friends +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (username != null ? !username.equals(user.username) : user.username != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (sex != null ? !sex.equals(user.sex) : user.sex != null) return false;
        if (birthday != null ? !birthday.equals(user.birthday) : user.birthday != null) return false;
        if (age != null ? !age.equals(user.age) : user.age != null) return false;
        if (weight != null ? !weight.equals(user.weight) : user.weight != null) return false;
        if (height != null ? !height.equals(user.height) : user.height != null) return false;
        if (phone != null ? !phone.equals(user.phone) : user.phone != null) return false;
        if (registerTime != null ? !registerTime.equals(user.registerTime) : user.registerTime != null) return false;
        if (lastLoginTime != null ? !lastLoginTime.equals(user.lastLoginTime) : user.lastLoginTime != null)
            return false;
        if (likeFoods != null ? !likeFoods.equals(user.likeFoods) : user.likeFoods != null) return false;
        if (friends != null ? !friends.equals(user.friends) : user.friends != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (sex != null ? sex.hashCode() : 0);
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        result = 31 * result + (age != null ? age.hashCode() : 0);
        result = 31 * result + (weight != null ? weight.hashCode() : 0);
        result = 31 * result + (height != null ? height.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (registerTime != null ? registerTime.hashCode() : 0);
        result = 31 * result + (lastLoginTime != null ? lastLoginTime.hashCode() : 0);
        result = 31 * result + (likeFoods != null ? likeFoods.hashCode() : 0);
        result = 31 * result + (friends != null ? friends.hashCode() : 0);
        return result;
    }
}
