package org.bracelet.entity;

import net.sf.json.JSONObject;

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
    @Column(name = "sleepType", length = 20)
    private String sleepType;

    public SleepState() {

    }

    public SleepState(String jsonString, User user) {
        super(jsonString, user);
        JSONObject json = JSONObject.fromString(jsonString);
        this.sleepType = json.getString("sleepType");
    }

    public String getSleepType() {
        return sleepType;
    }

    public void setSleepType(String sleepType) {
        this.sleepType = sleepType;
    }

    @Override
    public String toString() {
        JSONObject json = JSONObject.fromString(super.toString());
        json.put("sleepType", sleepType);
        return json.toString();
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
