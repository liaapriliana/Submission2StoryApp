package com.intermediate.storyapp1.ui.detail.createStory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.intermediate.storyapp1.data.model.CreateStoryResponse
import com.intermediate.storyapp1.data.presenter.StoryRepository
import com.intermediate.storyapp1.utils.DataDummy
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.File
import com.intermediate.storyapp1.data.presenter.Result
import com.intermediate.storyapp1.utils.getOrAwaitValue


@RunWith(MockitoJUnitRunner::class)
class CreateStoryViewmodelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var createStoryViewModel: CreateStoryViewmodel
    private var dummyResponse = DataDummy.generateDummyCreateStorySuccess()
    private var dummyDesc = "description".toRequestBody("text/plain".toMediaType())
    private var dummyLat = 0.01
    private var dummyLon = 0.01

    private val file: File = Mockito.mock(File::class.java)
    private val requestImageFile = file.asRequestBody("image/jpg".toMediaTypeOrNull())
    private val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
        "photo",
        file.name,
        requestImageFile
    )

    @Before
    fun setUp() {
        createStoryViewModel = CreateStoryViewmodel(storyRepository)
    }

    @Test
    fun `when Post Create Story Should Not Null and Return Success`() {
        val expectedCreateStory = MutableLiveData<Result<CreateStoryResponse>>()
        expectedCreateStory.value = Result.Success(dummyResponse)
        Mockito.`when`(
            storyRepository.createStory(
                imageFile = imageMultipart,
                desc = dummyDesc,
                lat = dummyLat,
                lon = dummyLon
            )
        ).thenReturn(expectedCreateStory)

        val actualResponse = createStoryViewModel.postCreateStory(
            imageFile = imageMultipart,
            desc = dummyDesc,
            lat = dummyLat,
            lon = dummyLon
        ).getOrAwaitValue()

        Mockito.verify(storyRepository).createStory(
            imageFile = imageMultipart,
            desc = dummyDesc,
            lat = dummyLat,
            lon = dummyLon
        )
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Success)
    }

    @Test
    fun `when Post Create Story Should Null and Return Error`() {
        dummyResponse = DataDummy.generateDummyCreateStoryError()

        val expectedCreateStory = MutableLiveData<Result<CreateStoryResponse>>()
        expectedCreateStory.value = Result.Error("error")
        Mockito.`when`(
            storyRepository.createStory(
                imageFile = imageMultipart,
                desc = dummyDesc,
                lat = dummyLat,
                lon = dummyLon
            )
        ).thenReturn(expectedCreateStory)

        val actualResponse = createStoryViewModel.postCreateStory(
            imageFile = imageMultipart,
            desc = dummyDesc,
            lat = dummyLat,
            lon = dummyLon
        ).getOrAwaitValue()

        Mockito.verify(storyRepository).createStory(
            imageFile = imageMultipart,
            desc = dummyDesc,
            lat = dummyLat,
            lon = dummyLon
        )
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Error)
    }
}