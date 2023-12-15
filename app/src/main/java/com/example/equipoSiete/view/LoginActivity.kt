package com.example.equipoSiete.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import androidx.fragment.app.viewModels
import com.example.equipoSiete.R
import com.example.equipoSiete.databinding.ActivityLoginBinding
import com.example.equipoSiete.viewmodel.LoginViewModel
import com.example.equipoSiete.widget.widget
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var passwordContainer: TextInputLayout
    private lateinit var emailEditText: TextInputEditText
    private lateinit var loginButton: MaterialButton
    private lateinit var registerButton: MaterialTextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)

        sharedPreferences = getSharedPreferences("shared", Context.MODE_PRIVATE)
        checkSession()
        sesion()
        setup()


        passwordContainer = findViewById(R.id.passwordContainer)
        passwordEditText = findViewById(R.id.passwordEditText)
        emailEditText = findViewById(R.id.emailEditText)
        loginButton = findViewById(R.id.btnLogin)
        registerButton= findViewById(R.id.tvRegister)

        // Inicialmente deshabilitar el botón de inicio de sesión
        loginButton.isEnabled = false
        registerButton.isEnabled= false
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

    private fun redirectToHomeScreen() {

        val homeIntent = Intent(this, MainActivity::class.java)
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(homeIntent)

        // Redirigir al usuario a la pantalla de inicio del teléfono
        val homeScreenIntent = Intent(Intent.ACTION_MAIN)
        homeScreenIntent.addCategory(Intent.CATEGORY_HOME)
        homeScreenIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(homeScreenIntent)

        // Finalizar la actividad de inicio de sesión
        finishAffinity()
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
        registerButton.isEnabled = emailFilled == true && passwordFilled
        if (registerButton.isEnabled) {
            registerButton.setTextColor(getColor(R.color.white))
            registerButton.setTypeface(null, android.graphics.Typeface.BOLD)
        }

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
        loginViewModel.registerUser(email,pass) { isRegister ->
            if (isRegister) {
                goToHome()
            } else {
                Toast.makeText(this, "Error en el registro", Toast.LENGTH_SHORT).show()
            }

        }
    }
    private fun goToHome(){
        val email = binding.emailEditText.text.toString()
        val intent = Intent (this, MainActivity::class.java).apply {
            putExtra("email",email)
        }
        startActivity(intent)
        finish()
    }
    private fun loginUser(){
        val email = binding.emailEditText.text.toString()
        val pass = binding.passwordEditText.text.toString()
        val fromWidget = intent.getBooleanExtra("fromWidget", false)

        loginViewModel.loginUser(email,pass){ isLogin ->
            if (isLogin){
                // Actualiza el TextView en el widget
                val widgetIntent = Intent(this, widget::class.java)
                widgetIntent.action = "LOGIN_SUCCESSFUL"
                sendBroadcast(widgetIntent)
                if (fromWidget){
                    redirectToHomeScreen()
                }else{
                    saveSession(email)
                    goToHome()
                }
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
                goToHome()
            }
        }
    }
    private fun saveSession(email: String) {
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.putBoolean("isLoggedIn", true)
        editor.apply()
    }


    private fun checkSession() {
        //val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        val firebaseAuth = FirebaseAuth.getInstance()
        val isLoggedIn = firebaseAuth.currentUser
        if ( isLoggedIn != null) {
            // El usuario ya ha iniciado sesión, navega a la actividad principal u otra actividad necesaria.
            goToHome()
        } else {
            // El usuario no ha iniciado sesión, muestra la interfaz de inicio de sesión.
        }
    }

    private fun createTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                // Verificar si ambos campos están vacíos para desactivar el botón
                val emailText = emailEditText.text.toString()
                val passwordText = passwordEditText.text.toString()

                binding.btnLogin.isEnabled = emailText.isNotEmpty() && passwordText.isNotEmpty()
                binding.tvRegister.isEnabled = emailText.isNotEmpty() && passwordText.isNotEmpty()
            }
        }
    }
    override fun onStart() {
        super.onStart()
        binding.clContenedor.visibility = View.VISIBLE
    }
}