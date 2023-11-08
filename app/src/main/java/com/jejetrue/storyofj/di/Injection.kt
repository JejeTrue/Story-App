package com.jejetrue.storyofj.di

import android.content.Context
import com.example.storysubmissionapp.data.local.database.StoryDatabase
import com.jejetrue.storyofj.data.api.retrofit.ApiConfig
import com.jejetrue.storyofj.data.pref.UserPreferences
import com.jejetrue.storyofj.data.pref.dataStore
import com.jejetrue.storyofj.data.repository.UserRepository

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val preference = UserPreferences.getInstance(context.dataStore)
        val database = StoryDatabase.getDatabase(context)
        return UserRepository.getInstance(preference,apiService, database)
    }


}