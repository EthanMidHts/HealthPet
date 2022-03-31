package com.ethanchris.android.healthpet.views;

import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ethanchris.android.healthpet.R;
import com.ethanchris.android.healthpet.models.PetColor;
import com.ethanchris.android.healthpet.models.PetHat;
import com.ethanchris.android.healthpet.models.User;
import com.ethanchris.android.healthpet.viewmodels.AuthViewModel;
import com.ethanchris.android.healthpet.viewmodels.AuthViewModelFactory;
import com.ethanchris.android.healthpet.viewmodels.GoogleFitViewModel;
import com.ethanchris.android.healthpet.viewmodels.GoogleFitViewModelFactory;
import com.ethanchris.android.healthpet.viewmodels.UserViewModel;
import com.ethanchris.android.healthpet.viewmodels.UserViewModelFactory;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Goal;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.GoalsReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.fitness.result.DataReadResult;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class PetScreenFragment extends Fragment {
    private Button mSettingsButton, mGoalsButton;
    private GoogleSignInAccount mAccount;
    private PetView mPetView;
    private UserViewModel mUserViewModel;
    private GoogleFitViewModel mGoogleFitViewModel;
    private PetColor mPetColor;
    private PetHat mPetHat;
    private FitnessOptions mFitnessOptions;
    private final int GOOGLE_PERMISSION_REQUEST_CODE = 2;

    private void loadUserAndPet(View view) {
        mUserViewModel.getFromFirebaseUser(FirebaseAuth.getInstance().getCurrentUser());
        mUserViewModel.getCurrentUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                mUserViewModel.getCurrentUser().removeObserver(this);
                mPetColor = user.getPetColor();
                mPetHat = user.getPetHat();
                mPetView = view.findViewById(R.id.petView);
                mPetView.setGif(getContext(), mPetColor, mPetHat);
            }
        });
    }

    private boolean hasActivityRecognitionPermission() {
        return (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED);
    }

    private ActivityResultCallback<Boolean> onGetPermissionResult = new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean accepted) {
            if (!accepted) {
                Toast.makeText(getContext(), "Permission needed :(", Toast.LENGTH_LONG).show();
                // Go back to home screen
                Intent intent = new Intent(getContext(), HomeScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    };

    private void getActivityRecognitionPermission(ActivityResultCallback<Boolean> callback) {
        Log.d("HealthPet", "asking for activity permission");
        // Ask for activity recognition permission
        ActivityResultLauncher arl = registerForActivityResult(new ActivityResultContracts.RequestPermission(), callback);
        arl.launch(Manifest.permission.ACTIVITY_RECOGNITION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GOOGLE_PERMISSION_REQUEST_CODE) {
            Log.d("HealthPet", "Got Google Fit permission result code " + resultCode);
            if (resultCode == Activity.RESULT_OK) {

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.pet_screen_fragment_fullscreen, container, false);

        mUserViewModel = new ViewModelProvider(this, new UserViewModelFactory())
                .get(UserViewModel.class);
        loadUserAndPet(view);
        mGoogleFitViewModel = new ViewModelProvider(this, new GoogleFitViewModelFactory())
                .get(GoogleFitViewModel.class);

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

        // Check for activity recognition permission
        if (!hasActivityRecognitionPermission()) {
            getActivityRecognitionPermission(onGetPermissionResult);
        }

        // Get Google account
        mFitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .build();
        mAccount = GoogleSignIn.getAccountForExtension(getContext(), mFitnessOptions);

        // Check if we have permission to get their fitness data
        if (!GoogleSignIn.hasPermissions(mAccount, mFitnessOptions)) {
            // If we don't, ask for permission
            GoogleSignIn.requestPermissions(this, GOOGLE_PERMISSION_REQUEST_CODE, mAccount, mFitnessOptions);
        } else {
            // Do something with Google Fit

            // Get user's fitness goals
            mGoogleFitViewModel.readGoals(getContext(), mAccount, new OnCompleteListener<List<Goal>>() {
                @Override
                public void onComplete(@NonNull Task<List<Goal>> task) {
                    List<Goal> goals = task.getResult();
                    for (Goal goal: goals) {
                        Goal.Recurrence recurrence = goal.getRecurrence();
                        if (recurrence.getUnit() == Goal.Recurrence.UNIT_DAY &&
                            recurrence.getCount() == 1) {
                            // Found goal that repeats once a day
                            if (goal.getMetricObjective().getDataTypeName().equals("com.google.step_count.delta")) {
                                // Found step count goal
                                int stepGoal = ((Double) goal.getMetricObjective().getValue()).intValue();
                                Log.d("HealthPet", "found step count goal of " + stepGoal + " steps");
                            }
                        }
                    }
                }
            });

            // Get user's step counts in last day
            mGoogleFitViewModel.readLastDayStepCounts(getContext(), mAccount, new OnCompleteListener<DataReadResponse>() {
                @Override
                public void onComplete(@NonNull Task<DataReadResponse> task) {
                    DataReadResponse readDataResult = task.getResult();
                    DataSet dataSet = readDataResult.getDataSet(DataType.AGGREGATE_STEP_COUNT_DELTA);
                    int totalSteps = 0;
                    for (DataPoint pt : dataSet.getDataPoints()) {
                        totalSteps += pt.getValue(Field.FIELD_STEPS).asInt();
                    }
                    Log.d("HealthPet", "total steps in last 24h: " + totalSteps);
                }
            });
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
