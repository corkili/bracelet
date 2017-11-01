package org.bracelet.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("sleep")
public class SleepState extends State {

    @Column(name = "sleepType")
    private String sleepType;

    public String getSleepType() {
        return sleepType;
    }

    public void setSleepType(String sleepType) {
        this.sleepType = sleepType;
    }
}
