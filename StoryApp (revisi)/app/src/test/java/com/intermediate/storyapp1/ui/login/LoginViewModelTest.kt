package com.intermediate.storyapp1.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.intermediate.storyapp1.data.model.LoginResponse
import com.intermediate.storyapp1.data.presenter.Result
import com.intermediate.storyapp1.data.presenter.StoryRepository
import com.intermediate.storyapp1.utils.DataDummy
import com.intermediate.storyapp1.utils.getOrAwaitValue
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest{
    private lateinit var loginViewModel: LoginViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    private var dummyLoginResponse = DataDummy.generateDummyLoginSuccess()
    private val dummyEmail = "email"
    private val dummyPassword = "password"

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(storyRepository)
    }

    @Test
    fun `when Post Login Should Not Null and Return Success`() {
        val expectedLogin = MutableLiveData<Result<LoginResponse>>()
        expectedLogin.value = Result.Success(dummyLoginResponse)
        Mockito.`when`(storyRepository.login(dummyEmail, dummyPassword)).thenReturn(expectedLogin)

        val actualResponse = loginViewModel.postLogin(dummyEmail, dummyPassword).getOrAwaitValue()

        Mockito.verify(storyRepository).login(dummyEmail, dummyPassword)
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Success)
    }

    @Test
    fun `when Post Login Should Null and Return Error`() {
        dummyLoginResponse = DataDummy.generateDummyLoginError()

        val expectedLogin = MutableLiveData<Result<LoginResponse>>()
        expectedLogin.value = Result.Error("invalid password")
        Mockito.`when`(storyRepository.login(dummyEmail, dummyPassword)).thenReturn(expectedLogin)

        val actualResponse = loginViewModel.postLogin(dummyEmail, dummyPassword).getOrAwaitValue()

        Mockito.verify(storyRepository).login(dummyEmail, dummyPassword)
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Error)
    }
}