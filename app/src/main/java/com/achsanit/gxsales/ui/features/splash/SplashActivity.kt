package com.achsanit.gxsales.ui.features.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.achsanit.gxsales.R
import com.achsanit.gxsales.ui.features.login.LoginActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                delay(2000L)

                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)

                finish()
            }
        }
    }
}