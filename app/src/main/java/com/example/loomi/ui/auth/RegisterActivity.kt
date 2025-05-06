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
import kotlin.math.abs

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

            var isValid = true
            binding.etUsername.error = null
            binding.etEmail.error = null
            binding.etPassword.error = null

            if (name.isEmpty()) {
                binding.etUsername.error = "Nama tidak boleh kosong"
                isValid = false
            }

            if (email.isEmpty()) {
                binding.etEmail.error = "Email tidak boleh kosong"
                isValid = false
            }

            if (password.isEmpty()) {
                binding.etPassword.error = "Password tidak boleh kosong"
                isValid = false
            } else if (password.length < 6) {
                binding.etPassword.error = "Password minimal 6 karakter"
                isValid = false
            }

            if (!isValid) return@setOnClickListener

            if (!agreeTerms) {
                Toast.makeText(this, "Harap setujui syarat dan ketentuan", Toast.LENGTH_SHORT)
                    .show()
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
                        assignInitialProfilePhoto(user)
                    }

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }

                is State.Error -> {
                    binding.progressBar.visibility = View.GONE
                    val message = when {
                        state.message.contains(
                            "email address is already in use",
                            ignoreCase = true
                        ) -> {
                            "Email sudah digunakan. Silakan gunakan email lain."
                        }

                        state.message.contains("network error", ignoreCase = true) -> {
                            "Terjadi masalah jaringan. Silakan coba lagi."
                        }

                        else -> {
                            "Registrasi gagal: ${state.message}"
                        }
                    }

                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }

            }
        }
    }

    private fun assignInitialProfilePhoto(user: FirebaseUser) {
        val email = user.email ?: return
        val initial = email.first().uppercaseChar()
        val backgroundColor = SOFT_BACKGROUND_COLORS.random()

        val photoUrl = buildAvatarUrl(initial.toString(), backgroundColor, TEXT_COLOR)

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setPhotoUri(photoUrl.toUri())
            .build()

        user.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("RegisterActivity", "Soft-profile photo assigned successfully")
                } else {
                    Log.e("RegisterActivity", "Failed to assign profile photo", task.exception)
                }
            }
    }

    private fun buildAvatarUrl(
        name: String,
        background: String,
        color: String
    ): String {
        return "https://ui-avatars.com/api/?" +
                "name=$name" +
                "&background=$background" +
                "&color=$color" +
                "&bold=true" +
                "&size=$AVATAR_SIZE"
    }
        companion object {

        private val SOFT_BACKGROUND_COLORS = listOf(
            "f0f0f0",
            "ffe9d6",
            "e8e8f7",
            "dbeafe",
            "e0f7f1",
            "f5f5dc"
        )

        private const val TEXT_COLOR = "555555"
        private const val AVATAR_SIZE = 256
    }

}
