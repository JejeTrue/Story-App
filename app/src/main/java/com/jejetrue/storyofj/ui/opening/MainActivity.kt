package com.jejetrue.storyofj.ui.opening

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import com.jejetrue.storyofj.databinding.ActivityMainBinding
import com.jejetrue.storyofj.ui.ViewModelFactory
import com.jejetrue.storyofj.ui.home.HomeActivity
import com.jejetrue.storyofj.ui.login.LoginActivity
import com.jejetrue.storyofj.ui.signup.SignupActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAction()
        setupAction()

    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener{
            startAuthActivity(true)
        }
        binding.signupButton.setOnClickListener {
            startAuthActivity(false)
        }
    }

    private fun startAuthActivity(showLogin: Boolean) {
        val intent = if (showLogin) {
            Intent(this, LoginActivity::class.java)
        } else {
            Intent(this, SignupActivity::class.java)
        }
        startActivity(intent)
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }








}