package com.jejetrue.storyofj.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storysubmissionapp.data.model.UserModel
import com.jejetrue.storyofj.data.repository.UserRepository

class MapsViewModel(private val repository: UserRepository) : ViewModel() {
    fun getSessionData(): LiveData<UserModel> = repository.getSession().asLiveData()

    fun getStories(token: String) = repository.getStoriesLocation(token)
}