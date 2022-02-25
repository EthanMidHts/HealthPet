package com.ethanchris.android.healthpet.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import com.ethanchris.android.healthpet.R

class PetScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Hide the title
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        // Hide the title bar
        supportActionBar!!.hide()
        // Enable full screen mode
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_pet_screen)

        Log.i("ActivityLifecycle", "PetScreenActivity onCreate()")
    }

    override fun onPause() {
        super.onPause()
        Log.i("ActivityLifecycle", "PetScreenActivity onPause()")
    }

    override fun onResume() {
        super.onResume()
        Log.i("ActivityLifecycle", "PetScreenActivity onResume()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("ActivityLifecycle", "PetScreenActivity onDestroy()")
    }
}