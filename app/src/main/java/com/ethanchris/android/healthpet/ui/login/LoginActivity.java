package com.ethanchris.android.healthpet.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ethanchris.android.healthpet.R;
import com.ethanchris.android.healthpet.databinding.ActivityLogin2Binding;
import com.ethanchris.android.healthpet.viewmodels.AuthViewModel;
import com.ethanchris.android.healthpet.viewmodels.AuthViewModelFactory;
import com.ethanchris.android.healthpet.viewmodels.UserViewModel;
import com.ethanchris.android.healthpet.viewmodels.UserViewModelFactory;
import com.ethanchris.android.healthpet.views.PetScreenActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private AuthViewModel mAuthViewModel;
    private UserViewModel mUserViewModel;
    private ActivityLogin2Binding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogin2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mUserViewModel = new ViewModelProvider(this, new UserViewModelFactory())
                .get(UserViewModel.class);
        mAuthViewModel = new ViewModelProvider(this, new AuthViewModelFactory(mUserViewModel))
                .get(AuthViewModel .class);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;

        Intent petIntent = new Intent(this, PetScreenActivity.class);
        petIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        mAuthViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                mAuthViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mAuthViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                mAuthViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });

        mAuthViewModel.getAuthResultTask().observe(this, new Observer<Task<AuthResult>>() {
            @Override
            public void onChanged(Task<AuthResult> authResultTask) {
                loadingProgressBar.setVisibility(View.INVISIBLE);
                if (authResultTask.isSuccessful()) {
                    updateUiWithUser(mAuthViewModel.getCurrentUser(), petIntent);
                } else {
                    Log.w("HealthPet", "LOGIN FAILED");
                    showLoginFailed(R.string.login_failed);
                }
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        Intent petIntent = new Intent(this, PetScreenActivity.class);
        petIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (mAuthViewModel.isLoggedIn()) {
            updateUiWithUser(mAuthViewModel.getCurrentUser(), petIntent);
        }
    }
    private void updateUiWithUser(FirebaseUser model, Intent petIntent) {
        String displayName = "";
        if (model.getDisplayName() != null) {
            displayName = " " + model.getDisplayName();
        }
        Toast.makeText(getApplicationContext(), "Welcome" + displayName + "!", Toast.LENGTH_LONG).show();
        startActivity(petIntent);
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        this.hideKeyboard();
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();

        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}