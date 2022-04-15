package com.ethanchris.android.healthpet.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ethanchris.android.healthpet.R;
import com.ethanchris.android.healthpet.viewmodels.AuthViewModel;
import com.ethanchris.android.healthpet.viewmodels.AuthViewModelFactory;
import com.ethanchris.android.healthpet.viewmodels.UserViewModel;
import com.ethanchris.android.healthpet.viewmodels.UserViewModelFactory;

public class SettingsActivity extends AppCompatActivity {

    private AuthViewModel mAuthViewModel;
    private UserViewModel mUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        mUserViewModel = new ViewModelProvider(this, new UserViewModelFactory())
                .get(UserViewModel.class);
         mAuthViewModel = new ViewModelProvider(this, new AuthViewModelFactory(mUserViewModel))
                .get(AuthViewModel .class);


        Button saveButton = findViewById(R.id.saveSettingsButton);
        Button logoutButton = findViewById(R.id.logoutButton);
        EditText displayNameEditText = findViewById(R.id.displayNameEditText);

        String currentDisplayName = mAuthViewModel.getCurrentUser().getDisplayName();
        if (currentDisplayName != null) {
            displayNameEditText.setText(currentDisplayName.toString());
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuthViewModel.updateDisplayName(displayNameEditText.getText().toString());
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuthViewModel.logout();
                // Go back to home screen
                Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        mAuthViewModel.getProfileUpdateSuccessful().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean successful) {
                if (successful) {
                    Toast.makeText(SettingsActivity.this, R.string.profile_update_successful, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SettingsActivity.this, R.string.profile_update_failure, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
