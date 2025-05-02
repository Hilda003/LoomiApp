package com.example.loomi.ui.auth



import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.loomi.utils.State
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException


class LoginViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _loginState = MutableLiveData<State<String>>()
    val loginState: LiveData<State<String>> = _loginState

    fun loginUser(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _loginState.value = State.Error("Email dan password tidak boleh kosong")
            return
        }

        _loginState.value = State.Loading

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _loginState.value = State.Success("Login berhasil!")
                } else {
                    val errorMessage = when (task.exception) {
                        is FirebaseAuthInvalidUserException,
                        is FirebaseAuthInvalidCredentialsException -> {
                            "Email atau password salah"
                        }
                        else -> {
                            "Terjadi kesalahan. Silakan coba lagi nanti"
                        }
                    }
                    _loginState.value = State.Error(errorMessage)
                }
            }

    }
}
