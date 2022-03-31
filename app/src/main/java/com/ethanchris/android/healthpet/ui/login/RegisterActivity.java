package com.ethanchris.android.healthpet.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ethanchris.android.healthpet.R;
import com.ethanchris.android.healthpet.models.PetColor;
import com.ethanchris.android.healthpet.viewmodels.AuthViewModel;
import com.ethanchris.android.healthpet.viewmodels.AuthViewModelFactory;
import com.ethanchris.android.healthpet.viewmodels.UserViewModel;
import com.ethanchris.android.healthpet.viewmodels.UserViewModelFactory;
import com.ethanchris.android.healthpet.views.PetScreenActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    AuthViewModel mAuthViewModel;
    UserViewModel mUserViewModel;

    EditText mEmailEditText;
    EditText mPasswordEditText;
    RadioGroup mPetColorRadioGroup;
    EditText mPetNameEditText;
    Button mRegisterButton;
    ProgressBar mProgressBar;

    private void setupViews() {
        mEmailEditText = findViewById(R.id.emailEditText);
        mPasswordEditText = findViewById(R.id.passwordEditText);
        mPetColorRadioGroup = findViewById(R.id.petColorRadioGroup);
        mPetNameEditText = findViewById(R.id.petNameEditText);
        mRegisterButton = findViewById(R.id.registerButton);
        mProgressBar = findViewById(R.id.loading);
    }

    private void updateUiWithUser(FirebaseUser model, Intent petIntent) {
        String displayName = "";
        if (model.getDisplayName() != null) {
            displayName = " " + model.getDisplayName();
        }
        Toast.makeText(getApplicationContext(), "Welcome" + displayName + "!", Toast.LENGTH_LONG).show();
        startActivity(petIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUserViewModel = new ViewModelProvider(this, new UserViewModelFactory())
                .get(UserViewModel.class);
        mAuthViewModel = new ViewModelProvider(this, new AuthViewModelFactory(mUserViewModel))
                .get(AuthViewModel .class);

        setupViews();

        // Setup register button
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressBar.setVisibility(View.VISIBLE);
                String email = mEmailEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                String petName = mPetNameEditText.getText().toString();
                int checkedButtonId = mPetColorRadioGroup.getCheckedRadioButtonId();
                PetColor petColor = PetColor.valueOf(((RadioButton) findViewById(checkedButtonId)).getText().toString().toUpperCase());
                mAuthViewModel.getAuthResultTask().observe(RegisterActivity.this, new Observer<Task<AuthResult>>() {
                    @Override
                    public void onChanged(Task<AuthResult> authResultTask) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        AuthResult result = authResultTask.getResult();
                        if (authResultTask.isSuccessful()) {
                            Intent petIntent = new Intent(RegisterActivity.this, PetScreenActivity.class);
                            updateUiWithUser(result.getUser(), petIntent);
                        }
                    }
                });
                mAuthViewModel.register(email, password, petName, petColor);
            }
        });
    }

}
