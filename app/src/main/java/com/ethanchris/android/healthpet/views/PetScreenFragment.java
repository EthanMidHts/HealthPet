package com.ethanchris.android.healthpet.views;

import android.Manifest;
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
import com.ethanchris.android.healthpet.viewmodels.UserViewModel;
import com.ethanchris.android.healthpet.viewmodels.UserViewModelFactory;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Goal;
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
    private PetColor mPetColor;
    private PetHat mPetHat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.pet_screen_fragment_fullscreen, container, false);

        mUserViewModel = new ViewModelProvider(this, new UserViewModelFactory())
                .get(UserViewModel.class);
        mUserViewModel.getFromFirebaseUser(FirebaseAuth.getInstance().getCurrentUser());
        mUserViewModel.getCurrentUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                mPetColor = user.getPetColor();
                mPetHat = user.getPetHat();
                mPetView = view.findViewById(R.id.petView);
                mPetView.setGif(getContext(), mPetColor, mPetHat);
            }
        });

        mSettingsButton = view.findViewById(R.id.open_settings_button);
        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View buttonView) {
                openSettingsActivity(view.getContext());
                mPetView = view.findViewById(R.id.petView);
            }
        });
        mGoalsButton = view.findViewById(R.id.open_goals_button);
        mGoalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View buttonView) { openGoalsActivity(view.getContext()); }
        });

        // Get activity recognition permission
        if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityResultLauncher arl = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean accepted) {
                    if (!accepted) {
                        Toast.makeText(view.getContext(), "Permission needed :(", Toast.LENGTH_LONG).show();
                        // Go back to home screen
                        Intent intent = new Intent(view.getContext(), HomeScreenActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
            });
            arl.launch(Manifest.permission.ACTIVITY_RECOGNITION);
        }

        setupGoogleFit();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setupGoogleFit() {
        FitnessOptions mFitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .build();
        mAccount = GoogleSignIn.getAccountForExtension(getActivity(), mFitnessOptions);
        if (!GoogleSignIn.hasPermissions(mAccount, mFitnessOptions)) {
            GoogleSignIn.requestPermissions(this, 2, mAccount, mFitnessOptions);
        } else {
            Log.d("HealthPet", "google fit access allowed");
            // access google fit
            Task<DataReadResponse> response = Fitness.getHistoryClient(getActivity(), mAccount)
                    .readData(new DataReadRequest.Builder()
                    .read(DataType.TYPE_STEP_COUNT_DELTA)
                    .setTimeRange(1000, System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                    .build());
                response.addOnCompleteListener(new OnCompleteListener<DataReadResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<DataReadResponse> task) {
                        DataReadResponse readDataResult = task.getResult();
                        DataSet dataSet = readDataResult.getDataSet(DataType.TYPE_STEP_COUNT_DELTA);
                        Log.d("HealthPet", dataSet.getDataPoints().toString());
                    }
                });

            Task<List<Goal>> goalResponse = Fitness.getGoalsClient(getActivity(), mAccount)
                    .readCurrentGoals(new GoalsReadRequest.Builder()
                            .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
                            .build());
            goalResponse.addOnCompleteListener(new OnCompleteListener<List<Goal>>() {
                @Override
                public void onComplete(@NonNull Task<List<Goal>> task) {
                    List<Goal> readDataResult = task.getResult();
                    Log.d("HealthPet", "found " + readDataResult.size() + " goals");
                    for (Goal g : readDataResult) {
                        Log.d("HealthPet", "goal: " + g.toString());
                    }
                }
            });

//            accessGoogleFit();
        }
    }

    private void accessGoogleFit() {
        Log.d("HealthPet", "accessing google fit");
        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.AGGREGATE_STEP_COUNT_DELTA)
                .setTimeRange(1000, System.currentTimeMillis() / 1000, TimeUnit.SECONDS)
                .bucketByTime(1, TimeUnit.DAYS)
                .build();
        Fitness.getHistoryClient(getActivity(), mAccount)
                .readData(readRequest)
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Log.d("HealthPet", "canceled");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("HealthPet", "failed", e);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<DataReadResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<DataReadResponse> task) {
                        if (!task.isSuccessful()) {
                            Log.d("HealthPet", "google fit failure", task.getException());
                        } else {
                            Log.d("HealthPet", "google fit success");
                            DataReadResponse responseData = task.getResult();
                            Log.d("HealthPet", responseData.getStatus().toString());
                        }
                    }
                });
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
