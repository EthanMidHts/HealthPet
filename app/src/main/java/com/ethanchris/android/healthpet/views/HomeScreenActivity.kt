package com.ethanchris.android.healthpet.views

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ethanchris.android.healthpet.R
import com.ethanchris.android.healthpet.ui.login.LoginActivity
import com.ethanchris.android.healthpet.ui.login.RegisterActivity

class HomeScreenActivity : AppCompatActivity() {
    private var mLoginButton: ImageButton? = null
    private var mRegisterButton: ImageButton? = null
    private var mAboutButton: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        val loginIntent = Intent(this, LoginActivity::class.java)
        mLoginButton = findViewById(R.id.loginButton)
        mLoginButton?.setOnClickListener {
            startActivity(loginIntent)
        }

        val registerIntent = Intent(this, RegisterActivity::class.java)
        mRegisterButton = findViewById(R.id.registerButton)
        mRegisterButton?.setOnClickListener {
            startActivity(registerIntent);
        }

        val aboutIntent = Intent(this, AboutActivity::class.java)
        mAboutButton = findViewById(R.id.aboutButton)
        mAboutButton?.setOnClickListener {
            startActivity(aboutIntent);
        }
    }
}