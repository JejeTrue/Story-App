package com.jejetrue.storyofj.ui.signup

import androidx.lifecycle.ViewModel
import com.jejetrue.storyofj.data.repository.UserRepository


class SignupViewModel(private val repository: UserRepository) : ViewModel() {
    fun register(name: String, email: String, password: String) =
        repository.registerUser(name, email, password)

    fun login(email: String, password: String) =
        repository.loginUser(email, password)



}