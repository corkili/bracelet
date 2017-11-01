package org.bracelet.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("walk")
public class SportState extends State{

    @Column(name = "sportType")
    private String sportType;

    @Column(name = "steps")
    private Long steps;

    @Column(name = "kilometre")
    private Double kilometre;

    public String getSportType() {
        return sportType;
    }

    public void setSportType(String sportType) {
        this.sportType = sportType;
    }

    public Long getSteps() {
        return steps;
    }

    public void setSteps(Long steps) {
        this.steps = steps;
    }

    public Double getKilometre() {
        return kilometre;
    }

    public void setKilometre(Double kilometre) {
        this.kilometre = kilometre;
    }
}
