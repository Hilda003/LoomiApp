package com.example.loomi.ui.auth



import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.loomi.utils.State
import com.google.firebase.auth.FirebaseAuth


class LoginViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _loginState = MutableLiveData<State<String>>()
    val loginState: LiveData<State<String>> = _loginState

    fun loginUser(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _loginState.value = State.Error("Please enter email and password")
            return
        }

        _loginState.value = State.Loading

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _loginState.value = State.Success("Login successful!")
                } else {
                    _loginState.value = State.Error(task.exception?.message ?: "Login failed")
                }
            }
    }
}
