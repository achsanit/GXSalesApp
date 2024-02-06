package com.achsanit.gxsales.ui.features.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.achsanit.gxsales.R
import com.achsanit.gxsales.data.MainRepository
import com.achsanit.gxsales.ui.features.login.LoginActivity
import com.achsanit.gxsales.ui.features.main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SplashActivity : AppCompatActivity() {

    private val mainRepo: MainRepository by inject() // get repository by koin inject
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                delay(2000L) // delay for 2 seconds

                // check user already login or not yet,
                // if already login start intent to main activity else start intent to login activity
                val intent = Intent(
                    this@SplashActivity,
                    if (mainRepo.isLogin()) MainActivity::class.java else LoginActivity::class.java
                )
                startActivity(intent)

                finish() // kill current activity
            }
        }
    }
}