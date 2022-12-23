package com.intermediate.storyapp1.ui.detail.createStory

import androidx.lifecycle.ViewModel
import com.intermediate.storyapp1.data.presenter.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CreateStoryViewmodel(private val storyRepository: StoryRepository): ViewModel() {
    fun postCreateStory(imageFile: MultipartBody.Part, desc: RequestBody, lat: Double, lon: Double) = storyRepository.createStory(imageFile, desc, lat, lon)
}