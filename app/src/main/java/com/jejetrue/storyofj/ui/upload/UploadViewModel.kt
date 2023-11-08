package com.jejetrue.storyofj.ui.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storysubmissionapp.data.model.UserModel
import com.jejetrue.storyofj.data.repository.UserRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody


class UploadViewModel constructor(private val  repository: UserRepository) : ViewModel() {

    fun uploadImage(
        token: String,
        imageUri: MultipartBody.Part,
        description: RequestBody
    ) = repository.uploadImage(token, imageUri, description)

    fun getSessionData(): LiveData<UserModel> =
        repository.getSession().asLiveData()
}