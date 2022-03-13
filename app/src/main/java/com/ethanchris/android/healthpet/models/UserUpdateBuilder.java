package com.ethanchris.android.healthpet.models;

public class UserUpdateBuilder {
    private int goalPoints;
    private String petName;
    private String petColor;
    private String goalName;
    private int goalCurrentProgress;
    private int goalTotalGoal;

    public UserUpdateBuilder setGoalPoints(int goalPoints) {
        this.goalPoints = goalPoints;
        return this;
    }

    public UserUpdateBuilder setPetName(String petName) {
        this.goalPoints = goalPoints;
        return this;
    }
}
