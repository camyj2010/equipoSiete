package com.example.equiposiete.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.equiposiete.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.example.equiposiete.databinding.ActivityLoginBinding
import com.example.equiposiete.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var passwordEditText: TextInputEditText
    private lateinit var passwordContainer: TextInputLayout
    private lateinit var emailEditText: TextInputEditText
    private lateinit var loginButton: MaterialButton
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)

        sharedPreferences = getSharedPreferences("shared", Context.MODE_PRIVATE)
        sesion()
        setup()

        passwordContainer = findViewById(R.id.passwordContainer)
        passwordEditText = findViewById(R.id.passwordEditText)
        emailEditText = findViewById(R.id.emailEditText)
        loginButton = findViewById(R.id.btnLogin)

        // Inicialmente deshabilitar el botón de inicio de sesión
        loginButton.isEnabled = false

        // Agregar TextWatcher para validar la contraseña en tiempo real
        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // No se necesita en este caso
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                validatePassword(charSequence.toString())
                updateLoginButtonState()
            }

            override fun afterTextChanged(editable: Editable?) {
                // No se necesita en este caso
            }
        })

        // Agregar TextWatcher para validar el correo electrónico en tiempo real
        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // No se necesita en este caso
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                updateLoginButtonState()
            }

            override fun afterTextChanged(editable: Editable?) {
                // No se necesita en este caso
            }
        })
    }

    private fun validatePassword(password: String) {
        if (password.length in 1..5) {
            passwordContainer.error = "Mínimo 6 dígitos"
            passwordContainer.boxStrokeColor = getColor(R.color.error_color)
        } else {
            passwordContainer.error = null
            passwordContainer.boxStrokeColor = getColor(R.color.white)
        }
    }

    private fun updateLoginButtonState() {
        val emailFilled = emailEditText.text?.isNotEmpty()
        val passwordFilled = passwordEditText.text!!.length >=6

        loginButton.isEnabled = emailFilled == true && passwordFilled

        if (loginButton.isEnabled) {
            loginButton.setTextColor(getColor(R.color.white))
            loginButton.setTypeface(null, android.graphics.Typeface.BOLD)
        }
    }

    private fun setup() {
        binding.tvRegister.setOnClickListener {
            registerUser()
        }

        binding.btnLogin.setOnClickListener {
            loginUser()
        }
    }
    private fun registerUser(){
        val email = binding.emailEditText.text.toString()
        val pass = binding.passwordEditText.text.toString()
        print("1")
        print(email)
        print(pass)
        loginViewModel.registerUser(email,pass) { isRegister ->
            if (isRegister) {
                goToHome(email)
            } else {
                Toast.makeText(this, "Error en el registro", Toast.LENGTH_SHORT).show()
            }

        }
    }
    private fun goToHome(email: String?){
        val intent = Intent (this, HomeActivity::class.java).apply {
            putExtra("email",email)
        }
        startActivity(intent)
        finish()
    }
    private fun loginUser(){
        val email = binding.emailEditText.text.toString()
        val pass = binding.passwordEditText.text.toString()
        loginViewModel.loginUser(email,pass){ isLogin ->
            if (isLogin){
                goToHome(email)
            }else {
                Toast.makeText(this, "Login incorrecto", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun sesion(){
        val email = sharedPreferences.getString("email",null)
        loginViewModel.sesion(email){ isEnableView ->
            if (isEnableView){
                binding.clContenedor.visibility = View.INVISIBLE
                goToHome(email)
            }
        }
    }
    override fun onStart() {
        super.onStart()
        binding.clContenedor.visibility = View.VISIBLE
    }
}

