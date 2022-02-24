package com.ethanchris.android.healthpet.views

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.ethanchris.android.healthpet.R
import com.ethanchris.android.healthpet.ui.login.LoginActivity

class HomeScreenActivity : AppCompatActivity() {
    private var loginButton: Button? = null
    private var createButton: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        val loginIntent = Intent(this, LoginActivity::class.java)
        loginButton = findViewById(R.id.login)
        loginButton?.setOnClickListener {
            startActivity(loginIntent)
        }
    }
}