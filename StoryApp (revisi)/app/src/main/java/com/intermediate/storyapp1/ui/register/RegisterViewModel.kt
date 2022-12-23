package com.intermediate.storyapp1.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intermediate.storyapp1.data.api.ApiConfig
import com.intermediate.storyapp1.data.model.RegisterResponse
import com.intermediate.storyapp1.data.presenter.StoryRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel (private val storyRepository: StoryRepository): ViewModel() {
    fun postRegister(name: String, email: String, password: String) = storyRepository.register(name, email, password)
}