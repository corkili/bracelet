package org.bracelet.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 心率状态
 */
@Entity
@DiscriminatorValue("heart")
public class HeartState extends State {

    /**
     * 心跳频率
     */
    @Column(name = "times")
    private Integer times;

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    @Override
    public String toString() {
        return "HeartState{" +
                "times=" + times +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HeartState)) return false;
        if (!super.equals(o)) return false;

        HeartState that = (HeartState) o;

        if (times != null ? !times.equals(that.times) : that.times != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (times != null ? times.hashCode() : 0);
        return result;
    }
}
