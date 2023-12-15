package com.example.equipoSiete.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.equipoSiete.repository.InventoryRepository
import com.example.equipoSiete.repository.LoginRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel  @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {
    //private val repository = LoginRepository()

    //registerUser se comunica con el repository
    fun registerUser(email: String, pass: String, isRegister: (Boolean) -> Unit) {
        loginRepository.registerUser(email, pass) { response ->
            isRegister(response)
        }
    }

    fun loginUser(email: String, pass: String, isLogin: (Boolean) -> Unit) {

        if (email.isNotEmpty() && pass.isNotEmpty()) {
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        isLogin(true)
                    } else {
                        isLogin(false)
                    }
                }
        } else {
            isLogin(false)
        }
    }

    fun sesion(email: String?, isEnableView: (Boolean) -> Unit) {
        if (email != null) {
            isEnableView(true)
        } else {
            isEnableView(false)
        }
    }
}