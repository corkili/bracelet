package org.bracelet.entity;

import javax.persistence.*;

@Entity
@Table(name = "food")
public class Food {

    /**
     * 食物ID
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 食物名称
     */
    @Column(name = "name", length = 30)
    private String name;

    /**
     * 热量（千卡/100g）
     */
    @Column(name = "heatContent", scale = 2)
    private Integer heatContent;

    /**
     * 硫胺素（毫克/100g）
     */
    @Column(name = "thiamine", scale = 2)
    private Double thiamine;

    /**
     * 钙（毫克/100g）
     */
    @Column(name = "calcium", scale = 2)
    private Double calcium;

    /**
     * 蛋白质（克/100g）
     */
    @Column(name = "protein", scale = 2)
    private Double protein;

    /**
     * 核黄素（毫克/100g）
     */
    @Column(name = "riboflavin", scale = 2)
    private Double riboflavin;

    /**
     * 镁（毫克/100g）
     */
    @Column(name = "magnesium", scale = 2)
    private Double magnesium;

    /**
     * 脂肪（克/100g）
     */
    @Column(name = "fat", scale = 2)
    private Double fat;

    /**
     * 烟酸（毫克/100g）
     */
    @Column(name = "niacin", scale = 2)
    private Double niacin;

    /**
     * 铁（毫克/100g）
     */
    @Column(name = "iron", scale = 2)
    private Double iron;

    /**
     * 碳水化合物（克/100g）
     */
    @Column(name = "carbohydrate", scale = 2)
    private Double carbohydrate;

    /**
     * 维生素C（毫克/100g）
     */
    @Column(name = "vitaminC", scale = 2)
    private Double vitaminC;

    /**
     * 猛（毫克/100g）
     */
    @Column(name = "manganese", scale = 2)
    private Double manganese;

    /**
     * 膳食纤维（克/100g）
     */
    @Column(name = "dietaryFibre", scale = 2)
    private Double dietaryFibre;

    /**
     * 维生素E（毫克/100g）
     */
    @Column(name = "vitaminE", scale = 2)
    private Double vitaminE;

    /**
     * 锌（毫克/100g）
     */
    @Column(name = "zinc", scale = 2)
    private Double zinc;

    /**
     * 维生素A（微克/100g）
     */
    @Column(name = "vitaminA", scale = 2)
    private Double vitaminA;

    /**
     * 胆固醇（毫克/100g）
     */
    @Column(name = "cholesterol", scale = 2)
    private Double cholesterol;

    /**
     * 铜（毫克/100g）
     */
    @Column(name = "copper", scale = 2)
    private Double copper;

    /**
     * 胡萝卜素（微克/100g）
     */
    @Column(name = "carotene", scale = 2)
    private Double carotene;

    /**
     * 钾（毫克/100g）
     */
    @Column(name = "potassium", scale = 2)
    private Double potassium;

    /**
     * 磷（毫克/100g）
     */
    @Column(name = "phosphorus", scale = 2)
    private Double phosphorus;

    /**
     * 视黄醇当量（微克/100g）
     */
    @Column(name = "retinolEquivalent", scale = 2)
    private Double retinolEquivalent;


    /**
     * 钠（毫克/100g）
     */
    @Column(name = "sodium", scale = 2)
    private Double sodium;

    /**
     * 硒（微克/100g）
     */
    @Column(name = "selenium", scale = 2)
    private Double selenium;

    /**
     * 食物所属类型
     */
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "foodTypeId")
    private FoodType foodType;

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

    public Integer getHeatContent() {
        return heatContent;
    }

    public void setHeatContent(Integer heatContent) {
        this.heatContent = heatContent;
    }

    public Double getThiamine() {
        return thiamine;
    }

    public void setThiamine(Double thiamine) {
        this.thiamine = thiamine;
    }

    public Double getCalcium() {
        return calcium;
    }

    public void setCalcium(Double calcium) {
        this.calcium = calcium;
    }

    public Double getProtein() {
        return protein;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }

    public Double getRiboflavin() {
        return riboflavin;
    }

    public void setRiboflavin(Double riboflavin) {
        this.riboflavin = riboflavin;
    }

    public Double getMagnesium() {
        return magnesium;
    }

    public void setMagnesium(Double magnesium) {
        this.magnesium = magnesium;
    }

    public Double getFat() {
        return fat;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

    public Double getNiacin() {
        return niacin;
    }

    public void setNiacin(Double niacin) {
        this.niacin = niacin;
    }

    public Double getIron() {
        return iron;
    }

    public void setIron(Double iron) {
        this.iron = iron;
    }

    public Double getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(Double carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public Double getVitaminC() {
        return vitaminC;
    }

    public void setVitaminC(Double vitaminC) {
        this.vitaminC = vitaminC;
    }

    public Double getManganese() {
        return manganese;
    }

    public void setManganese(Double manganese) {
        this.manganese = manganese;
    }

    public Double getDietaryFibre() {
        return dietaryFibre;
    }

    public void setDietaryFibre(Double dietaryFibre) {
        this.dietaryFibre = dietaryFibre;
    }

    public Double getVitaminE() {
        return vitaminE;
    }

    public void setVitaminE(Double vitaminE) {
        this.vitaminE = vitaminE;
    }

    public Double getZinc() {
        return zinc;
    }

    public void setZinc(Double zinc) {
        this.zinc = zinc;
    }

    public Double getVitaminA() {
        return vitaminA;
    }

    public void setVitaminA(Double vitaminA) {
        this.vitaminA = vitaminA;
    }

    public Double getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(Double cholesterol) {
        this.cholesterol = cholesterol;
    }

    public Double getCopper() {
        return copper;
    }

    public void setCopper(Double copper) {
        this.copper = copper;
    }

    public Double getCarotene() {
        return carotene;
    }

    public void setCarotene(Double carotene) {
        this.carotene = carotene;
    }

    public Double getPotassium() {
        return potassium;
    }

    public void setPotassium(Double potassium) {
        this.potassium = potassium;
    }

    public Double getPhosphorus() {
        return phosphorus;
    }

    public void setPhosphorus(Double phosphorus) {
        this.phosphorus = phosphorus;
    }

    public Double getRetinolEquivalent() {
        return retinolEquivalent;
    }

    public void setRetinolEquivalent(Double retinolEquivalent) {
        this.retinolEquivalent = retinolEquivalent;
    }

    public Double getSodium() {
        return sodium;
    }

    public void setSodium(Double sodium) {
        this.sodium = sodium;
    }

    public Double getSelenium() {
        return selenium;
    }

    public void setSelenium(Double selenium) {
        this.selenium = selenium;
    }

    public FoodType getFoodType() {
        return foodType;
    }

    public void setFoodType(FoodType foodType) {
        this.foodType = foodType;
    }

    @Override
    public String toString() {
        return "Food{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", heatContent=" + heatContent +
                ", thiamine=" + thiamine +
                ", calcium=" + calcium +
                ", protein=" + protein +
                ", riboflavin=" + riboflavin +
                ", magnesium=" + magnesium +
                ", fat=" + fat +
                ", niacin=" + niacin +
                ", iron=" + iron +
                ", carbohydrate=" + carbohydrate +
                ", vitaminC=" + vitaminC +
                ", manganese=" + manganese +
                ", dietaryFibre=" + dietaryFibre +
                ", vitaminE=" + vitaminE +
                ", zinc=" + zinc +
                ", vitaminA=" + vitaminA +
                ", cholesterol=" + cholesterol +
                ", copper=" + copper +
                ", carotene=" + carotene +
                ", potassium=" + potassium +
                ", phosphorus=" + phosphorus +
                ", retinolEquivalent=" + retinolEquivalent +
                ", sodium=" + sodium +
                ", selenium=" + selenium +
                '}';
    }

}
