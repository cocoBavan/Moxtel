package com.bltech.moxtel.global.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class SplashActivity: Activity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().also {
            it.setKeepOnScreenCondition { true }
        }
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
