package com.jejetrue.storyofj.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storysubmissionapp.data.model.UserModel
import com.jejetrue.storyofj.R
import com.jejetrue.storyofj.data.api.Result
import com.jejetrue.storyofj.databinding.ActivityHomeBinding
import com.jejetrue.storyofj.ui.map.MapsActivity
import com.jejetrue.storyofj.ui.ViewModelFactory
import com.jejetrue.storyofj.ui.login.LoginActivity
import com.jejetrue.storyofj.ui.opening.MainActivity
import com.jejetrue.storyofj.ui.upload.UploadActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSessionData().observe(this) { user ->
            if (!user.isLogin) {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                loadStories(user.token)
            }
        }

        binding.fabAdd.setOnClickListener{
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.toolbar.setOnMenuItemClickListener { menu ->
            when(menu.itemId){
                R.id.menu_map ->{
                    startActivity(Intent(this, MapsActivity::class.java))
                    true
                }
                R.id.menu_logout -> {
                    showDialog()
                    true
                }
                R.id.menu_language_change -> {
                    setting()
                    true
                }
                else -> true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding
    }

    private fun showDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Log Out")
            setMessage("Apa kamu yakin mau keluar?")
            setPositiveButton("Ya") { _, _ ->
                viewModel.logout()
                loginActivity()
            }
            create()
            show()
        }
    }

    private fun loginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setting() {
        val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
        startActivity(intent)
    }


    private fun loadStories(token : String) {
        viewModel.stories(token).observe(this) {
            val adapter = StoryListAdapter()
            binding.rvStory.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.rvStory.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            )
            adapter.submitData(lifecycle, it)
        }
    }
}