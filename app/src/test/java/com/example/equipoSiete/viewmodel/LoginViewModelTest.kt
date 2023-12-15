package com.example.equipoSiete.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.equipoSiete.repository.InventoryRepository
import com.example.equipoSiete.repository.LoginRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class LoginViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule() //código que involucra LiveData y ViewModel
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var loginRepository: LoginRepository

    @Before
    fun setUp() {
        loginRepository = mock(LoginRepository::class.java)
        loginViewModel = LoginViewModel(loginRepository)
    }

    @Test
    fun `test método registerUser`(){
        val email="test@example.com"
        val password="123456"
        val expectedResult=true

        //when
        `when`(loginRepository.registerUser(email,password){}).thenAnswer{
            val callback=it.arguments[2] as (Boolean)-> Unit
            callback.invoke(expectedResult)
        }
        //then
        loginViewModel.registerUser(email,password){
            result ->assert(result)
        }

    }
}