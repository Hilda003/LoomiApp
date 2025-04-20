package com.example.loomi.ui.auth


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.loomi.R
import com.example.loomi.databinding.ActivityRegisterBinding
import com.example.loomi.MainActivity
import com.example.loomi.utils.State
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.btnRegister.setOnClickListener {
            val name = binding.etUsername.editText?.text.toString().trim()
            val email = binding.etEmail.editText?.text.toString().trim()
            val password = binding.etPassword.editText?.text.toString().trim()
            val agreeTerms = binding.checkbox.isChecked

            if (!agreeTerms) {
                Toast.makeText(this, "You must agree with the terms and conditions", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.registerUser(name, email, password)
        }

        binding.txtSignIn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun observeViewModel() {
        viewModel.registerState.observe(this) { state ->
            when (state) {
                is State.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is State.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, state.data, Toast.LENGTH_SHORT).show()

                    val currentUser = FirebaseAuth.getInstance().currentUser
                    currentUser?.let { user ->
                        assignRandomProfilePhoto(user)
                    }

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                is State.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun assignRandomProfilePhoto(user: FirebaseUser) {
        val randomNumber = (1..70).random()
        val photoUrl = "https://i.pravatar.cc/150?img=$randomNumber"

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setPhotoUri(photoUrl.toUri())
            .build()

        user.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("RegisterActivity", "Random profile photo assigned successfully")
                } else {
                    Log.e("RegisterActivity", "Failed to assign profile photo", task.exception)
                }
            }
    }
}

