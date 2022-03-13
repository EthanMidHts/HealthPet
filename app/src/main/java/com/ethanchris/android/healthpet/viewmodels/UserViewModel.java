package com.ethanchris.android.healthpet.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ethanchris.android.healthpet.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserViewModel extends ViewModel {
    private FirebaseFirestore db;

    UserViewModel(FirebaseFirestore db) {
        this.db = db;
    }

    private MutableLiveData<User> currentUser = new MutableLiveData<>();

    public MutableLiveData<User> getCurrentUser() { return currentUser; }

    public void getFromFirebaseUser(FirebaseUser firebaseUser) {
        String uid = firebaseUser.getUid();
        db.collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                if (doc.exists()) {
                    Map<String, Object> data = doc.getData();
                    String goalName = (String) data.get(User.FB_GOAL_NAME);
                    String petID = (String) data.get(User.FB_PET_ID);
                    int goalPoints = (Integer) data.get(User.FB_GOAL_POINTS);
                    int goalCurrentProgress = (Integer) data.get(User.FB_GOAL_CURRENT_PROGRESS);
                    int goalTotalGoal = (Integer) data.get(User.FB_GOAL_TOTAL_GOAL);
                    User user = new User(firebaseUser, goalPoints, goalName, petID, goalCurrentProgress, goalTotalGoal);
                    currentUser.setValue(user);
                } else {
                    currentUser.setValue(null);
                }
            }
        });
    }

    public void createFromFirebaseUser(FirebaseUser user) {
        String uid = user.getUid();
        String goalName = "";
        int goalPoints = 0;
        int goalCurrentProgress = -1;
        int goalTotalGoal = -1;
        Map<String, Object> data = new HashMap<String, Object>();

        data.put(User.FB_GOAL_NAME, goalName);
        data.put(User.FB_GOAL_POINTS, goalPoints);
        data.put(User.FB_GOAL_CURRENT_PROGRESS, goalCurrentProgress);
        data.put(User.FB_GOAL_TOTAL_GOAL, goalTotalGoal);

        db.collection("users").document(uid).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }
}
