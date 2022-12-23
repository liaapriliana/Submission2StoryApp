package com.intermediate.storyapp1.ui.detail.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "You're Profile"
    }
    val text: LiveData<String> = _text
}