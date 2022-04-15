package com.ethanchris.android.healthpet.views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ethanchris.android.healthpet.R;
import com.ethanchris.android.healthpet.models.PetColor;
import com.ethanchris.android.healthpet.models.PetHat;
import com.ethanchris.android.healthpet.models.User;
import com.ethanchris.android.healthpet.viewmodels.UserViewModel;
import com.ethanchris.android.healthpet.viewmodels.UserViewModelFactory;
import com.google.firebase.auth.FirebaseAuth;

public class PetScreenFragment extends Fragment {
    private ImageButton mSettingsButton, mGoalsButton, mShopButton;
    private PetView mPetView;
    private UserViewModel mUserViewModel;
    private PetColor mPetColor;
    private PetHat mPetHat;

    private void loadUserAndPet(View view) {
        mUserViewModel.getFromFirebaseUser(FirebaseAuth.getInstance().getCurrentUser());
        mUserViewModel.getCurrentUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                mUserViewModel.getCurrentUser().removeObserver(this);
                mPetColor = user.getPetColor();
                mPetHat = user.getPetHat();
                mPetView.setGif(getContext(), mPetColor, mPetHat);
                mPetView.setName(user.getPetName());
                mPetView.setSpeechCallback(new PetCallback<PetViewSpeechResponseType>() {
                    @Override
                    public void handleCallback(PetViewSpeechResponseType value) {
                        if (value == PetViewSpeechResponseType.GOAL_ACCOMPLISHED) {
                            handleGoalAccomplished();
                        } else if (value == PetViewSpeechResponseType.GOAL_NOT_ACCOMPLISHED) {
                            handleGoalNotAccomplished();
                        }
                    }
                });
            }
        });
    }

    private void handleGoalAccomplished() {
        User user = mUserViewModel.getCurrentUser().getValue();
        user.setDaysLeftInGoal((int) user.getDaysLeftInGoal() - 1);
        if (user.getDaysLeftInGoal() == 0) {
            // User has accomplished their goal, reset it and award them points
            user.setGoalName("No Goal");
            user.setTotalDaysInGoal(-1);
            user.setGoalPoints((int)user.getGoalPoints() + 5);
            user.setDaysLeftInGoal(-1);
            Toast.makeText(getContext(), "Congratulations! You completed your goal.", Toast.LENGTH_SHORT).show();
        }
        mUserViewModel.saveUser(user);
    }

    private void handleGoalNotAccomplished() {
        User user = mUserViewModel.getCurrentUser().getValue();
        user.setDaysLeftInGoal((int) user.getTotalDaysInGoal());
        mUserViewModel.saveUser(user);
    }

    private boolean hasRecordAudioPermission() {
        return (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED);
    }

    private ActivityResultCallback<Boolean> onGetPermissionResult = new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean accepted) {
            if (!accepted) {
                Toast.makeText(getContext(), "Permission needed :(", Toast.LENGTH_LONG).show();
                // Go back to home screen if permission was denied
                Intent intent = new Intent(getContext(), HomeScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    };

    private void getRecordAudioPermission(ActivityResultCallback<Boolean> callback) {
        Log.d("HealthPet", "asking for record audio permission");
        ActivityResultLauncher arl = registerForActivityResult(new ActivityResultContracts.RequestPermission(), callback);
        arl.launch(Manifest.permission.RECORD_AUDIO);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPetView = getView().findViewById(R.id.petView);
        loadUserAndPet(mPetView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.pet_screen_fragment_fullscreen, container, false);

        mUserViewModel = new ViewModelProvider(this, new UserViewModelFactory())
                .get(UserViewModel.class);

        mPetView = view.findViewById(R.id.petView);
        loadUserAndPet(view);

        mSettingsButton = view.findViewById(R.id.open_settings_button);
        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View buttonView) {
                openSettingsActivity(view.getContext());
            }
        });
        mGoalsButton = view.findViewById(R.id.open_goals_button);
        mGoalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View buttonView) { openGoalsActivity(view.getContext()); }
        });

        mShopButton = view.findViewById(R.id.open_shop_button);
        mShopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View buttonView) {
                openShopActivity(view.getContext());
            }
        });

        // Check for record audio permission and obtain it if needed
        if (!hasRecordAudioPermission()) {
            getRecordAudioPermission(onGetPermissionResult);
        }

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void openSettingsActivity(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        startActivity(intent);
    }

    private void openGoalsActivity(Context context) {
        Intent intent = new Intent(context, GoalDetailActivity.class);
        startActivity(intent);
    }

    private void openShopActivity(Context context) {
        Intent intent = new Intent(context, ShopActivity.class);
        startActivity(intent);
    }
}
