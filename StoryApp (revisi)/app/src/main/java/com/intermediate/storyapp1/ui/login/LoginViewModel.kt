package com.intermediate.storyapp1.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intermediate.storyapp1.data.api.ApiConfig
import com.intermediate.storyapp1.data.model.LoginResponse
import com.intermediate.storyapp1.data.presenter.StoryRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel (private val storyRepository: StoryRepository): ViewModel() {
    fun postLogin(email: String, password: String) = storyRepository.login(email, password)
}