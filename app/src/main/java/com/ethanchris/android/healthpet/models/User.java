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
    public static final String FB_PET_ID = "petID";
    public static final String FB_GOAL_POINTS = "goalPoints";
    public static final String FB_GOAL_CURRENT_PROGRESS = "goalCurrentProgress";
    public static final String FB_GOAL_TOTAL_GOAL = "goalTotalGoal";

    private int goalPoints;
    private String goalName;
    private String petID;
    private int goalCurrentProgress;
    private int goalTotalGoal;

    private FirebaseUser user;

    private User() {}

    public User(FirebaseUser firebaseUser) {
        this.goalPoints = 0;
        this.goalName = "";
        this.petID = "";
        this.goalCurrentProgress = -1;
        this.goalTotalGoal = -1;
        this.user = firebaseUser;
    }

    public User(FirebaseUser firebaseUser, int goalPoints, String goalName, String petID, int goalCurrentProgress, int goalTotalGoal) {
        this.user = firebaseUser;
        this.goalName = goalName;
        this.petID = petID;
        this.goalPoints = goalPoints;
        this.goalCurrentProgress = goalCurrentProgress;
        this.goalTotalGoal = goalTotalGoal;
    }

    public int getGoalPoints() {
        return goalPoints;
    }

    public String getGoalName() {
        return goalName;
    }

    public String getPetID() {
        return petID;
    }

    public int getGoalCurrentProgress() {
        return goalCurrentProgress;
    }

    public int getGoalTotalGoal() {
        return goalTotalGoal;
    }

    public String getName() {
        return this.user.getDisplayName();
    }
}
