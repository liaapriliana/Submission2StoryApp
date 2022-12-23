package com.intermediate.storyapp1.ui.maps

import androidx.lifecycle.ViewModel
import com.intermediate.storyapp1.data.presenter.StoryRepository

class MapsViewModel (private val storyRepository: StoryRepository) : ViewModel() {
    fun getStories() = storyRepository.getStories()
}