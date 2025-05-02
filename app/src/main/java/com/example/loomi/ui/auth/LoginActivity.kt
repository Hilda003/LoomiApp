package com.example.loomi.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.loomi.R
import com.example.loomi.databinding.ActivityLoginBinding
import com.example.loomi.MainActivity
import com.example.loomi.utils.State
import com.google.firebase.auth.FirebaseAuth



class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupLoginButton()
        observeLoginState()
        setupNavigation()
    }

    private fun setupLoginButton() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.editText?.text.toString().trim()
            val password = binding.etPassword.editText?.text.toString().trim()

            var isValid = true
            binding.etEmail.error = null
            binding.etPassword.error = null

            if (email.isEmpty()) {
                binding.etEmail.error = "Email tidak boleh kosong"
                isValid = false
            }

            if (password.isEmpty()) {
                binding.etPassword.error = "Password tidak boleh kosong"
                isValid = false
            }

            if (!isValid) return@setOnClickListener

            viewModel.loginUser(email, password)
        }
    }

    private fun observeLoginState() {
        viewModel.loginState.observe(this) { state ->
            when (state) {
                is State.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnLogin.isEnabled = false
                }
                is State.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnLogin.isEnabled = true
                    Toast.makeText(this, state.data, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                is State.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnLogin.isEnabled = true
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupNavigation() {
        binding.txtRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.txtForgotPassword.setOnClickListener {
            val email = binding.etEmail.editText?.text.toString().trim()
            if (email.isEmpty()) {
                binding.etEmail.error = "Silakan masukkan email"
            } else {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Password reset sudah dikirim", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this, task.exception?.message ?: "Gagal mengirim reset email", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }
}


