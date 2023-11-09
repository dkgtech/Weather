package com.dkgtech.weather.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.dkgtech.weather.R
import com.dkgtech.weather.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding) {
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }, 2000)
        }
    }
}