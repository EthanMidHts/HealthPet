package com.ethanchris.android.healthpet.models;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class User {
    public static final String FB_GOAL_NAME = "goalName";
    public static final String FB_PET_NAME = "petName";
    public static final String FB_GOAL_POINTS = "goalPoints";
    public static final String FB_GOAL_CURRENT_PROGRESS = "goalCurrentProgress";
    public static final String FB_GOAL_TOTAL_GOAL = "goalTotalGoal";
    public static final String FB_PET_COLOR = "petColor";
    public static final String FB_PET_HAT = "petHat";

    private long goalPoints;
    private String goalName;
    private String petName;
    private PetColor petColor;
    private PetHat petHat;
    private long goalCurrentProgress;
    private long goalTotalGoal;

    private FirebaseUser user;

    private User() {}

    public User(FirebaseUser firebaseUser, long goalPoints, String goalName, String petName, long goalCurrentProgress, long goalTotalGoal, PetColor petColor, PetHat petHat) {
        this.user = firebaseUser;
        this.goalName = goalName;
        this.goalPoints = goalPoints;
        this.goalCurrentProgress = goalCurrentProgress;
        this.goalTotalGoal = goalTotalGoal;
        this.petName = petName;
        this.petColor = petColor;
        this.petHat = petHat;
    }

    public long getGoalPoints() {
        return goalPoints;
    }

    public void setGoalPoints(int goalPoints) {
        this.goalPoints = goalPoints;
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public long getGoalCurrentProgress() {
        return goalCurrentProgress;
    }

    public void setGoalCurrentProgress(int goalCurrentProgress) {
        this.goalCurrentProgress = goalCurrentProgress;
    }

    public long getGoalTotalGoal() {
        return this.goalTotalGoal;
    }

    public void setGoalTotalGoal(int goalTotalGoal) {
        this.goalTotalGoal = goalTotalGoal;
    }

    public String getName() {
        return this.user.getDisplayName();
    }

    public String getPetName() { return this.petName; };

    public PetColor getPetColor() { return this.petColor; }

    public PetHat getPetHat() { return this.petHat; };

    public FirebaseUser getFirebaseUser() { return this.user; }
}
