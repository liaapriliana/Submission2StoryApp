package com.intermediate.storyapp1.ui.stories

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.intermediate.storyapp1.data.api.ApiConfig
import com.intermediate.storyapp1.data.model.LoginPreference
import com.intermediate.storyapp1.data.model.Story
import com.intermediate.storyapp1.data.model.StoryResponse
import com.intermediate.storyapp1.data.presenter.StoryRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel (repo: StoryRepository): ViewModel() {
    val getListStory: LiveData<PagingData<Story>> =
        repo.getListStories().cachedIn(viewModelScope)
}