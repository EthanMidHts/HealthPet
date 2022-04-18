package com.ethanchris.android.healthpet.views;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ethanchris.android.healthpet.R;
import com.ethanchris.android.healthpet.models.User;
import com.ethanchris.android.healthpet.viewmodels.HabitGoalPopupViewModel;
import com.ethanchris.android.healthpet.viewmodels.HabitGoalPopupViewModelFactory;
import com.ethanchris.android.healthpet.viewmodels.UserViewModel;
import com.ethanchris.android.healthpet.viewmodels.UserViewModelFactory;
import com.google.firebase.auth.FirebaseAuth;
import android.graphics.Typeface;

public class GoalDetailActivity extends AppCompatActivity {
    ImageButton mCreateHabitGoal, mClearGoal;
    UserViewModel mUserViewModel;
    HabitGoalPopupViewModel mHabitGoalPopupViewModel;
    AlertDialog mDialog;
    TextView mGoalNameTextView;
    ProgressBar mGoalProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_detail);

        mUserViewModel = new ViewModelProvider(this, new UserViewModelFactory())
                .get(UserViewModel.class);
        if (mUserViewModel.getCurrentUser().getValue() == null) {
            mUserViewModel.getFromFirebaseUser(FirebaseAuth.getInstance().getCurrentUser());
            mUserViewModel.getCurrentUser().observe(this, new Observer<User>() {
                @Override
                public void onChanged(User user) {

                }
            });
        }

        mHabitGoalPopupViewModel = new ViewModelProvider(this, new HabitGoalPopupViewModelFactory()).get(HabitGoalPopupViewModel.class);
        mClearGoal = findViewById(R.id.clearCurrentGoalButton);
        mCreateHabitGoal = findViewById(R.id.createHabitGoalButton);
        mGoalProgressBar = findViewById(R.id.goalProgressBar);

        mGoalNameTextView = findViewById(R.id.goalNameTextView);
        mGoalNameTextView.setText(mUserViewModel.getCurrentUser().getValue().getGoalName());
        Typeface typeface = ResourcesCompat.getFont(
                this,
                R.font.backto1984);
        mGoalNameTextView.setTypeface(typeface);
        mUserViewModel.getCurrentUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                mGoalNameTextView.setText(user.getGoalName());
                if (user.getGoalName().equals("No Goal")) {
                    mClearGoal.setVisibility(View.INVISIBLE);
                    mGoalProgressBar.setVisibility(View.INVISIBLE);
                    mCreateHabitGoal.setVisibility(View.VISIBLE);
                } else {
                    mClearGoal.setVisibility(View.VISIBLE);
                    mGoalProgressBar.setVisibility(View.VISIBLE);
                    mCreateHabitGoal.setVisibility(View.INVISIBLE);
                    // Calculate progress
                    int progress = (int)Math.floor(((user.getTotalDaysInGoal() - user.getDaysLeftInGoal()) / ((double) user.getTotalDaysInGoal())) * 100);
                    mGoalProgressBar.setProgress(progress);
                }
            }
        });

        mClearGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clearGoal()) {
                    mUserViewModel.getUserSaved().observe(GoalDetailActivity.this, new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean success) {
                            mUserViewModel.getUserSaved().removeObservers(GoalDetailActivity.this);
                            if (success) Toast.makeText(GoalDetailActivity.this, "Cleared goal successfully!", Toast.LENGTH_SHORT).show();
                            else Toast.makeText(GoalDetailActivity.this, "Failed to clear goal", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(GoalDetailActivity.this, "Failed to clear goal", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mCreateHabitGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GoalDetailActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View popupView = inflater.inflate(R.layout.popup_create_habit_goal, null);

                EditText habitNameEditText = popupView.findViewById(R.id.habitNameEditText);
                habitNameEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        String newText = charSequence.toString();
                        mHabitGoalPopupViewModel.getHabitName().setValue(newText);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) { }
                });

                EditText habitNumDaysEditText = popupView.findViewById(R.id.habitNumDaysEditText);
                habitNumDaysEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        int newNumDays = Integer.parseInt(charSequence.toString());
                        mHabitGoalPopupViewModel.getNumDays().setValue(newNumDays);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) { }
                });

                AlertDialog dialog = builder.setView(popupView).setPositiveButton(R.string.create_button, null)
                .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).setTitle("Create habit goal").create();

                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (createHabitGoal()) {
                            mUserViewModel.getUserSaved().observe(GoalDetailActivity.this, new Observer<Boolean>() {
                                @Override
                                public void onChanged(Boolean success) {
                                    mUserViewModel.getUserSaved().removeObservers(GoalDetailActivity.this);
                                    if (success) Toast.makeText(GoalDetailActivity.this, "Set goal successfully!", Toast.LENGTH_SHORT).show();
                                    else Toast.makeText(GoalDetailActivity.this, "Failed to set goal", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            });
                        } else {
                            Toast.makeText(GoalDetailActivity.this, "Failed to set goal", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    }
                });
            }
        });
    }

    private boolean clearGoal() {
        User user = mUserViewModel.getCurrentUser().getValue();
        if (user != null) {
            user.setGoalName("No Goal");
            user.setDaysLeftInGoal(-1);
            mUserViewModel.saveUser(user);
            return true;
        } else {
            return false;
        }
    }

    private boolean createHabitGoal() {
        User user = mUserViewModel.getCurrentUser().getValue();
        if (user != null) {
            user.setGoalName(mHabitGoalPopupViewModel.getHabitName().getValue());
            user.setDaysLeftInGoal(mHabitGoalPopupViewModel.getNumDays().getValue());
            user.setTotalDaysInGoal(mHabitGoalPopupViewModel.getNumDays().getValue());
            mUserViewModel.saveUser(user);
            return true;
        } else {
            return false;
        }
    }
}
