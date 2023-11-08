package com.jejetrue.storyofj.ui.home

import androidx.lifecycle.LiveData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storysubmissionapp.data.model.Story
import com.example.storysubmissionapp.data.model.UserModel
import com.jejetrue.storyofj.data.repository.UserRepository
import kotlinx.coroutines.launch


class HomeViewModel(private val repository: UserRepository) : ViewModel() {
    fun getSessionData() : LiveData<UserModel> = repository.getSession().asLiveData()
    fun stories(token:String): LiveData<PagingData<Story>> =  repository.getStories(token).cachedIn(viewModelScope)
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}