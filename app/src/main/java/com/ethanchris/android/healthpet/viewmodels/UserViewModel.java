package com.ethanchris.android.healthpet.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ethanchris.android.healthpet.models.PetColor;
import com.ethanchris.android.healthpet.models.PetHat;
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
        if (UserViewModel.user != null) {
            this.currentUser.setValue(UserViewModel.user);
        }
        this.db = db;
    }

    private MutableLiveData<User> currentUser = new MutableLiveData<>();
    public MutableLiveData<User> getCurrentUser() { return currentUser; }

    private MutableLiveData<Boolean> userInitialized = new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> getUserInitialized() { return userInitialized; }

    private MutableLiveData<Boolean> userSaved = new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> getUserSaved() { return userSaved; }

    private static User user;

    public void clearSingletonUser() {
        this.user = null;
    }

    public void saveUser(User user) {
        this.user = user;
        this.currentUser.setValue(user);
        if (user == null) {
            Log.d("HealthPet", "cannot save null user");
            return;
        }

        Map<String, Object> data = new HashMap<String, Object>();

        data.put(User.FB_GOAL_NAME, user.getGoalName());
        data.put(User.FB_GOAL_POINTS, user.getGoalPoints());
        data.put(User.FB_GOAL_CURRENT_PROGRESS, user.getGoalCurrentProgress());
        data.put(User.FB_GOAL_TOTAL_GOAL, user.getGoalTotalGoal());
        data.put(User.FB_PET_NAME, user.getPetName());
        data.put(User.FB_PET_HAT, user.getPetHat().name());
        data.put(User.FB_PET_COLOR, user.getPetColor().name());

        db.collection("users").document(user.getFirebaseUser().getUid()).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    UserViewModel.this.userSaved.setValue(true);
                    Log.d("HealthPet", "saved User in Firebase");
                } else {
                    UserViewModel.this.userSaved.setValue(false);
                    Log.d("HealthPet", "failed to save User in firebase", task.getException());
                }
            }
        });
    }

    public void getFromFirebaseUser(FirebaseUser firebaseUser) {
        if (user != null) {
            currentUser.setValue(user);
        } else {
            String uid = firebaseUser.getUid();
            db.collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        Map<String, Object> data = doc.getData();
                        String goalName = (String) data.get(User.FB_GOAL_NAME);
                        String petName = (String) data.get(User.FB_PET_NAME);
                        PetColor petColor = PetColor.valueOf((String) data.get(User.FB_PET_COLOR));
                        PetHat petHat = PetHat.valueOf((String) data.get(User.FB_PET_HAT));
                        long goalPoints = (Long) data.get(User.FB_GOAL_POINTS);
                        long goalCurrentProgress = (Long) data.get(User.FB_GOAL_CURRENT_PROGRESS);
                        long goalTotalGoal = (Long) data.get(User.FB_GOAL_TOTAL_GOAL);
                        User user = new User(firebaseUser, goalPoints, goalName, petName, goalCurrentProgress, goalTotalGoal, petColor, petHat);
                        currentUser.setValue(user);
                        UserViewModel.user = user;
                    } else {
                        UserViewModel.user = null;
                        currentUser.setValue(null);
                    }
                }
            });
        }
    }

    public void initializeUser(FirebaseUser user, String petName, PetColor petColor) {
        String uid = user.getUid();
        String goalName = "";
        int goalPoints = 999;
        int goalCurrentProgress = -1;
        int goalTotalGoal = -1;
        Map<String, Object> data = new HashMap<String, Object>();

        data.put(User.FB_GOAL_NAME, goalName);
        data.put(User.FB_GOAL_POINTS, goalPoints);
        data.put(User.FB_GOAL_CURRENT_PROGRESS, goalCurrentProgress);
        data.put(User.FB_GOAL_TOTAL_GOAL, goalTotalGoal);
        data.put(User.FB_PET_NAME, petName);
        data.put(User.FB_PET_HAT, PetHat.NO_HAT.name());
        data.put(User.FB_PET_COLOR, petColor.name());

        db.collection("users").document(uid).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    UserViewModel.this.userInitialized.setValue(true);
                    Log.d("HealthPet", "created new User in Firebase");
                } else {
                    UserViewModel.this.userInitialized.setValue(false);
                    Log.d("HealthPet", "failed to create new User in firebase", task.getException());
                }
            }
        });
    }
}
