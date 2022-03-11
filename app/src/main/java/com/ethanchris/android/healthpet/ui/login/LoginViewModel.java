package com.ethanchris.android.healthpet.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.content.Intent;
import android.util.Patterns;

import com.ethanchris.android.healthpet.data.LoginRepository;
import com.ethanchris.android.healthpet.data.Result;
import com.ethanchris.android.healthpet.data.model.LoggedInUser;
import com.ethanchris.android.healthpet.R;
import com.ethanchris.android.healthpet.views.PetScreenActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;
    private FirebaseAuth mAuth;

    LoginViewModel(LoginRepository loginRepository, FirebaseAuth mAuth) {
        this.mAuth = mAuth;
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        if(mAuth.getCurrentUser().getEmail() == username){
            try {
                mAuth.signInWithEmailAndPassword(username, password);
            } catch (Exception FirebaseAuthInvalidCredentialsException) {
                loginResult.setValue(new LoginResult(R.string.login_failed));
            }
        } else {
            mAuth.createUserWithEmailAndPassword(username, password);
        }
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