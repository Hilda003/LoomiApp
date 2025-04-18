package com.example.loomi.ui.auth


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.loomi.utils.State
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class RegisterViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _registerState = MutableLiveData<State<String>>()
    val registerState: LiveData<State<String>> = _registerState

    fun registerUser(name: String, email: String, password: String) {
        if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
            _registerState.value = State.Error("Please fill in all fields")
            return
        }

        _registerState.value = State.Loading

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                _registerState.value = State.Success("Register Success!")
                            } else {
                                _registerState.value = State.Error(updateTask.exception?.message ?: "Failed to update profile")
                            }
                        }
                } else {
                    _registerState.value = State.Error(task.exception?.message ?: "Register Failed")
                }
            }
    }
}
