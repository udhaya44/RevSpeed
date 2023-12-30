package com.revspeed.models;

public class Plan {

    private int planId;
    private String planName;
    private int speed;
    private String dataLimit;
    private Double price;

    public Plan(){
        // Default constructor with no arguments
    }
    public Plan(int planId, String planName, int speed, String dataLimit, Double price) {
        this.planId = planId;
        this.planName = planName;
        this.speed = speed;
        this.dataLimit = dataLimit;
        this.price = price;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getDataLimit() {
        return dataLimit;
    }

    public void setDataLimit(String dataLimit) {
        this.dataLimit = dataLimit;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Plan{" +
                "planId=" + planId +
                ", planName='" + planName + '\'' +
                ", speed=" + speed +
                ", dataLimit=" + dataLimit +
                ", price=" + price +
                '}';
    }
}
