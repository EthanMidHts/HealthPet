package com.ethanchris.android.healthpet.views;

import android.content.Context;
import android.os.Bundle;

import com.ethanchris.android.healthpet.databinding.ActivityShopBinding;
import com.ethanchris.android.healthpet.models.User;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ethanchris.android.healthpet.R;
import com.ethanchris.android.healthpet.models.PetColor;
import com.ethanchris.android.healthpet.models.PetHat;
import com.ethanchris.android.healthpet.models.User;
import com.ethanchris.android.healthpet.viewmodels.UserViewModel;
import com.ethanchris.android.healthpet.viewmodels.UserViewModelFactory;
import com.google.firebase.auth.FirebaseAuth;

import com.ethanchris.android.healthpet.R;


public class ShopActivity extends AppCompatActivity {
    private UserViewModel mUserViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        ImageButton noHatButton = findViewById(R.id.nohatbutton);
        ImageButton topHatButton = findViewById(R.id.tophatbutton);
        ImageButton baseballHatButton = findViewById(R.id.baseballbutton);
        ImageButton cowboyHatButton = findViewById(R.id.cowboybutton);

        mUserViewModel = new ViewModelProvider(this, new UserViewModelFactory())
                .get(UserViewModel.class);
        User user = mUserViewModel.getCurrentUser().getValue();

        TextView pointsTextView = (TextView)findViewById(R.id.pointsText);
        pointsTextView.setText(Long.toString(user.getGoalPoints()));

        Context context = getApplicationContext();
        CharSequence toastText = "Hat set!";
        int toastDuration = Toast.LENGTH_SHORT;


        noHatButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                user.setPetHat(PetHat.NO_HAT);
                mUserViewModel.saveUser(user);
                Toast toast = Toast.makeText(context, toastText, toastDuration);
                toast.show();
            }
        });

        topHatButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(user.getTopHat()){
                    user.setPetHat(PetHat.TOP_HAT);
                    mUserViewModel.saveUser(user);
                    Toast toast = Toast.makeText(context, toastText, toastDuration);
                    toast.show();} else if(user.getGoalPoints() > 1){
                    user.setPetHat(PetHat.TOP_HAT);
                    int points = (int) (user.getGoalPoints() - 1);
                    user.setGoalPoints(points);
                    user.setTopHat(true);
                    mUserViewModel.saveUser(user);
                    Toast toast = Toast.makeText(context, toastText, toastDuration);
                    pointsTextView.setText(Long.toString(user.getGoalPoints()));
                } else {
                    CharSequence toastText = "Insufficient points.";
                    Toast toast = Toast.makeText(context, toastText, toastDuration);
                }
            }
        });

        baseballHatButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(user.getbaseBallHat()){
                    user.setPetHat(PetHat.BASEBALL_HAT);
                    mUserViewModel.saveUser(user);
                    Toast toast = Toast.makeText(context, toastText, toastDuration);
                    toast.show();} else if(user.getGoalPoints() > 2){
                    user.setPetHat(PetHat.TOP_HAT);
                    int points = (int) (user.getGoalPoints() - 2);
                    user.setGoalPoints(points);
                    user.setbaseBallHat(true);
                    mUserViewModel.saveUser(user);
                    Toast toast = Toast.makeText(context, toastText, toastDuration);
                    pointsTextView.setText(Long.toString(user.getGoalPoints()));
                } else {
                    CharSequence toastText = "Insufficient points.";
                    Toast toast = Toast.makeText(context, toastText, toastDuration);
                }
            }
        });

        cowboyHatButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(user.getCowboyHat()){
                    user.setPetHat(PetHat.COWBOY_HAT);
                    mUserViewModel.saveUser(user);
                    Toast toast = Toast.makeText(context, toastText, toastDuration);
                    toast.show();} else if(user.getGoalPoints() > 3){
                    user.setPetHat(PetHat.TOP_HAT);
                    int points = (int) (user.getGoalPoints() - 3);
                    user.setGoalPoints(points);
                    user.setCowboyHat(true);
                    mUserViewModel.saveUser(user);
                    Toast toast = Toast.makeText(context, toastText, toastDuration);
                    pointsTextView.setText(Long.toString(user.getGoalPoints()));
                } else {
                    CharSequence toastText = "Insufficient points.";
                    Toast toast = Toast.makeText(context, toastText, toastDuration);
                }
            }
        });

    }
}