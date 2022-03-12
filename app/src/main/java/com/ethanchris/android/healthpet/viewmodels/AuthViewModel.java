package com.ethanchris.android.healthpet.viewmodels;

import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.ethanchris.android.healthpet.R;
import com.ethanchris.android.healthpet.ui.login.LoginFormState;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends ViewModel {
    private final MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<Task<AuthResult>> authResultTask = new MutableLiveData<>();

    private FirebaseAuth mAuth;

    AuthViewModel(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }
    public LiveData<Task<AuthResult>> getAuthResultTask() { return authResultTask; }

    public void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("HealthPet", "signInWithEmailAndPassword:success");
                            authResultTask.setValue(task);
                        } else {
                            Log.w("HealthPet", "signInWithEmailAndPassword:failure", task.getException());
                            register(email, password);
                        }
                    }
                });
    }

    public void register(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        authResultTask.setValue(task);
                        if (task.isSuccessful()) {
                            Log.d("HealthPet", "createUserWithEmailAndPassword:success");
                        } else {
                            Log.w("HealthPet", "createUserWithEmailAndPassword:failure", task.getException());
                        }
                    }
                });
    }

    public Boolean isLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

}
