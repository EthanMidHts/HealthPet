package com.ethanchris.android.healthpet.views

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ethanchris.android.healthpet.R

class PetScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_screen)


        val fm = supportFragmentManager;
        var fragment = fm.findFragmentById(R.id.pet_screen_fragment_container)
        // Make sure fragment isn't already created
        if (fragment == null) {
            fragment = PetScreenFragment()
            fm.beginTransaction().add(R.id.pet_screen_fragment_container, fragment).commit()
        }

        Log.i("ActivityLifecycle", "PetScreenActivity onCreate()")
    }

    override fun onBackPressed() {
        // do nothing
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