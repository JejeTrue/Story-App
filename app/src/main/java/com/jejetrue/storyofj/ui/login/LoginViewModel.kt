package com.jejetrue.storyofj.ui.login

import androidx.lifecycle.ViewModel
import com.jejetrue.storyofj.data.repository.UserRepository


class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    fun login(email: String, password: String) =
        repository.loginUser(email, password)
}