package com.intermediate.storyapp1.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intermediate.storyapp1.data.injection.Injection
import com.intermediate.storyapp1.data.presenter.StoryRepository
import com.intermediate.storyapp1.ui.detail.createStory.CreateStoryViewmodel
import com.intermediate.storyapp1.ui.login.LoginViewModel
import com.intermediate.storyapp1.ui.register.RegisterViewModel
import com.intermediate.storyapp1.ui.stories.StoryViewModel

class ViewModelFactory private constructor(private val repo: StoryRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            return StoryViewModel(repo) as T
        }
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repo) as T
        }
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(repo) as T
        }
        if (modelClass.isAssignableFrom(CreateStoryViewmodel::class.java)) {
            return CreateStoryViewmodel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
        }
    }
}