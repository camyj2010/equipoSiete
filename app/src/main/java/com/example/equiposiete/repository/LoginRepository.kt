package com.example.equiposiete.repository

import com.google.firebase.auth.FirebaseAuth

class LoginRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()

    fun registerUser(email: String, pass:String, isRegisterComplete: (Boolean)->Unit){
        print("3")
        print(email)
        print(pass)
        if(email.isNotEmpty() && pass.isNotEmpty()){
            firebaseAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        isRegisterComplete(true)
                    } else {
                        isRegisterComplete(false)
                    }
                }
        }else{
            isRegisterComplete(false)
        }
    }
}