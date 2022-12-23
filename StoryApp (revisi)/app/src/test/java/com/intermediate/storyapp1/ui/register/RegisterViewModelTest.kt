package com.intermediate.storyapp1.ui.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.intermediate.storyapp1.data.model.RegisterResponse
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
class RegisterViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var registerViewModel: RegisterViewModel
    private var dummyRegisterResponse = DataDummy.generateDummyRegisterSuccess()

    private val dummyName = "name"
    private val dummyEmail = "email"
    private val dummyPassword = "password"

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(storyRepository)
    }

    @Test
    fun `when Post Register Should Not Null and Return Success`() {
        val expectedRegister = MutableLiveData<Result<RegisterResponse>>()
        expectedRegister.value = Result.Success(dummyRegisterResponse)
        Mockito.`when`(storyRepository.register(dummyName, dummyEmail, dummyPassword)).thenReturn(expectedRegister)

        val actualResponse = registerViewModel.postRegister(dummyName, dummyEmail, dummyPassword).getOrAwaitValue()

        Mockito.verify(storyRepository).register(dummyName, dummyEmail, dummyPassword)
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Success)
    }

    @Test
    fun `when Post Register Should Null and Return Error`() {
        dummyRegisterResponse = DataDummy.generateDummyRegisterError()

        val expectedRegister = MutableLiveData<Result<RegisterResponse>>()
        expectedRegister.value = Result.Error("bad request")
        Mockito.`when`(storyRepository.register(dummyName, dummyEmail, dummyPassword)).thenReturn(expectedRegister)

        val actualResponse = registerViewModel.postRegister(dummyName, dummyEmail, dummyPassword).getOrAwaitValue()

        Mockito.verify(storyRepository).register(dummyName, dummyEmail, dummyPassword)
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Error)
    }

}