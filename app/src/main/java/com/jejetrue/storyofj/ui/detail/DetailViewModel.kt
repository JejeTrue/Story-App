package com.jejetrue.storyofj.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.jejetrue.storyofj.data.repository.UserRepository

class DetailViewModel constructor(private val repository: UserRepository) : ViewModel() {

    fun getDetailStory(token: String, id: String) = repository.getDetailStory(token, id)
    fun getSessionData() = repository.getSession().asLiveData()
}