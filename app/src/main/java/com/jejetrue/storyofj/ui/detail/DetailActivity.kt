package com.jejetrue.storyofj.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.jejetrue.storyofj.data.api.Result
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.jejetrue.storyofj.databinding.ActivityDetailBinding
import com.jejetrue.storyofj.loadImage
import com.jejetrue.storyofj.ui.ViewModelFactory

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyId = intent.getStringExtra(ID)

        if (storyId != null) {
            viewModel.getSessionData().observe(this) { user ->
                loadStory(user.token, storyId)
            }
        }
    }

    private fun loadStory(token: String, id: String) {
        viewModel.getDetailStory(token, id).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.isVisible = true
                    }

                    is Result.Error -> {
                        binding.progressBar.isVisible = false
                        showToast(result.error)
                    }

                    is Result.Success -> {
                        binding.progressBar.isVisible = false
                        val story = result.data.story
                        binding.apply {
                            titleStory.text = story.name
                            descStory.text = story.description
                            imageView.loadImage(this@DetailActivity, story.photoUrl)
                        }
                    }
                }
            }

        }

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val ID = "id_key"
    }
}