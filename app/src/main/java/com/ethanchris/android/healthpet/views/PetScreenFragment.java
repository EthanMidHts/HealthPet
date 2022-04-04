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
    private Button mSettingsButton, mGoalsButton;
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
            }
        });
    }

    private boolean hasRecordAudioPermission() {
        return (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED);
    }

    private ActivityResultCallback<Boolean> onGetPermissionResult = new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean accepted) {
            if (!accepted) {
                Toast.makeText(getContext(), "Permission needed :(", Toast.LENGTH_LONG).show();
//              Go back to home screen
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.pet_screen_fragment_fullscreen, container, false);

        mUserViewModel = new ViewModelProvider(this, new UserViewModelFactory())
                .get(UserViewModel.class);
        loadUserAndPet(view);

        mPetView = view.findViewById(R.id.petView);

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
}
