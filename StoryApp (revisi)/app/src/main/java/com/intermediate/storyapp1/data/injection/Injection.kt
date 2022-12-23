package com.intermediate.storyapp1.data.injection

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.intermediate.storyapp1.data.api.ApiConfig
import com.intermediate.storyapp1.data.model.LoginPreference
import com.intermediate.storyapp1.data.presenter.StoryRepository

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("stories")

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val preferences = LoginPreference(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository.getInstance(preferences, apiService)
    }
}