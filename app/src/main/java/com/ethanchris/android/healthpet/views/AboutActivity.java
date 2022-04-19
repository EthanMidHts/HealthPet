package com.ethanchris.android.healthpet.views;



import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import com.ethanchris.android.healthpet.databinding.ActivityShopBinding;
import com.ethanchris.android.healthpet.models.User;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import android.os.Bundle;

public class AboutActivity extends AppCompatActivity {
    TextView mAbout, mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        mAbout = findViewById(R.id.about);
        mDescription = findViewById(R.id.description);
        Typeface typeface = ResourcesCompat.getFont(
                this,
                R.font.backto1984);
        mAbout.setTypeface(typeface);
        mDescription.setTypeface(typeface);
    }
}