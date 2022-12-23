package com.intermediate.storyapp1.ui.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.intermediate.storyapp1.data.model.StoryResponse
import com.intermediate.storyapp1.data.presenter.StoryRepository
import com.intermediate.storyapp1.utils.DataDummy
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import com.intermediate.storyapp1.data.presenter.Result
import com.intermediate.storyapp1.utils.getOrAwaitValue


@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest
@get:Rule
val instantExecutorRule = InstantTaskExecutorRule()

@Mock
private lateinit var storyRepository: StoryRepository
private lateinit var mapViewModel: MapsViewModel
private var dummyStory = DataDummy.generateDummyStory()

@Before
fun setUp() {
    mapViewModel = MapsViewModel(storyRepository)
}

@Test
fun `when Get Story Should Not Null and Return Success`() {
    val expectedResponse = MutableLiveData<Result<StoryResponse>>()
    expectedResponse.value = Result.Success(dummyStory)
    Mockito.`when`(storyRepository.getStories()).thenReturn(expectedResponse)

    val actualResponse = mapViewModel.getStories().getOrAwaitValue()

    Mockito.verify(storyRepository).getStories()
    Assert.assertNotNull(actualResponse)
    Assert.assertTrue(actualResponse is Result.Success)
}

@Test
fun `when Get Story Should Null and Return Error`() {
    dummyStory = DataDummy.generateErrorDummyStory()

    val expectedResponse = MutableLiveData<Result<StoryResponse>>()
    expectedResponse.value = Result.Error("error")
    Mockito.`when`(storyRepository.getStories()).thenReturn(expectedResponse)

    val actualResponse = mapViewModel.getStories().getOrAwaitValue()

    Mockito.verify(storyRepository).getStories()
    Assert.assertNotNull(actualResponse)
    Assert.assertTrue(actualResponse is Result.Error)
}
