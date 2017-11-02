package org.bracelet.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 睡眠状态
 */
@Entity
@DiscriminatorValue("sleep")
public class SleepState extends State {

    /**
     * 睡眠类型（深睡眠或浅睡眠）
     */
    @Column(name = "sleepType")
    private String sleepType;

    public String getSleepType() {
        return sleepType;
    }

    public void setSleepType(String sleepType) {
        this.sleepType = sleepType;
    }

    @Override
    public String toString() {
        return "SleepState{" +
                "sleepType='" + sleepType + '\'' +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SleepState)) return false;
        if (!super.equals(o)) return false;

        SleepState that = (SleepState) o;

        if (sleepType != null ? !sleepType.equals(that.sleepType) : that.sleepType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (sleepType != null ? sleepType.hashCode() : 0);
        return result;
    }
}
