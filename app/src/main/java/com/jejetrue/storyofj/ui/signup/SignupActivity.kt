package com.jejetrue.storyofj.ui.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jejetrue.storyofj.R
import com.jejetrue.storyofj.ValidateType
import com.jejetrue.storyofj.data.api.Result
import com.jejetrue.storyofj.databinding.ActivitySignupBinding
import com.jejetrue.storyofj.showToast
import com.jejetrue.storyofj.ui.ViewModelFactory
import com.jejetrue.storyofj.ui.login.LoginActivity
import com.jejetrue.storyofj.validate

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val viewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupAction()
        setupView()
        playAnimation()
    }

    private fun setupAction() {
        binding.apply {
            signupButton.setOnClickListener {
                if(validasi()){
                    val name = nameEditText.text.toString()
                    val email = emailEditText.text.toString()
                    val password = passwordEditText.text.toString()

                    viewModel.register(name, email, password).observe(this@SignupActivity){result ->
                        if(result != null) {
                            when(result) {
                                is Result.Loading ->{
                                    showLoading(true)
                                    signupButton.isEnabled = false
                                }

                                is Result.Error ->{
                                    showLoading(false)
                                    progressBar.isEnabled = false
                                    showToast(result.error)

                                }

                                is Result.Success ->{
                                    showLoading(false)
                                    progressBar.isVisible = false
                                    MaterialAlertDialogBuilder(this@SignupActivity)
                                        .setTitle("akun kamu berhasil daftar")
                                        .setMessage("Mohon untuk Login aku kamu")
                                        .setPositiveButton("Login") { dialog, _ ->
                                            dialog.dismiss()
                                            val intent =
                                                Intent(
                                                    this@SignupActivity,
                                                    LoginActivity::class.java
                                                )
                                            intent.flags =
                                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            startActivity(intent)
                                        }
                                        .create().show()

                                }
                            }
                        }

                    }
                }
            }
        }
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


    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup
            )
            startDelay = 100
        }.start()
    }




    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.signupButton.isEnabled = !isLoading
    }

    private fun validasi() : Boolean{
        binding.apply {
            val validates = listOf(
                emailEditText.validate("Email", ValidateType.REQUIRED),
                emailEditText.validate("Email", ValidateType.EMAIL),
                nameEditText.validate("Name", ValidateType.REQUIRED),
                passwordEditText.validate("Password", ValidateType.REQUIRED),
                passwordEditText.validate("Password", ValidateType.MIN_CHAR),
            )

            return !validates.contains(false)
        }
    }


}