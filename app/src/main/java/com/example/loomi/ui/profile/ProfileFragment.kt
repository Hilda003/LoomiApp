package com.example.loomi.ui.profile

import android.content.Context
import com.example.loomi.ui.auth.LoginActivity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.loomi.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.loomi.R
import com.example.loomi.databinding.DialogLanguageBinding
import com.example.loomi.utils.LocaleHelper
import java.util.Locale


class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        sharedPreferences = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)

        displayUserInfo()
        setupLanguageToggle()
        setupLogoutButton()

        return binding.root
    }

    private fun displayUserInfo() {
        val user = auth.currentUser
        if (user != null) {
            binding.txtUsername.text = user.displayName ?: "No Name"
            binding.txtEmail.text = user.email ?: "No Email"

            val photoUrl = user.photoUrl
            if (photoUrl != null) {
                Glide.with(this)
                    .load(photoUrl)
                    .placeholder(R.drawable.circle_background)
                    .into(binding.ivProfile)
            } else {
                val email = user.email ?: "?"
                val initial = email.firstOrNull()?.uppercaseChar()?.toString() ?: "?"
                binding.ivProfile.setImageBitmap(generateInitialBitmap(initial))
            }
        } else {
//            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun generateInitialBitmap(initial: String): Bitmap {
        val size = 200
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val paintCircle = Paint().apply {
            color = Color.LTGRAY
            isAntiAlias = true
        }

        val paintText = Paint().apply {
            color = Color.WHITE
            textSize = 80f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        canvas.drawCircle(size / 2f, size / 2f, size / 2f, paintCircle)
        val yPos = (canvas.height / 2 - (paintText.descent() + paintText.ascent()) / 2)
        canvas.drawText(initial, size / 2f, yPos, paintText)

        return bitmap
    }

    private fun setupLanguageToggle() {
        binding.btnLanguage.setOnClickListener {
            showLanguageDialog()
        }
    }

    private fun setupLogoutButton() {
        binding.btnLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun showLanguageDialog() {
        val dialogBinding = DialogLanguageBinding.inflate(LayoutInflater.from(requireContext()))
        val currentLang = sharedPreferences.getString("app_lang", "en")

        when (currentLang) {
            "en" -> dialogBinding.radioEnglish.isChecked = true
            "id" -> dialogBinding.radioIndonesian.isChecked = true
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setPositiveButton("OK") { _, _ ->
                val selectedLang = when (dialogBinding.radioGroupLanguage.checkedRadioButtonId) {
                    R.id.radioEnglish -> "en"
                    R.id.radioIndonesian -> "id"
                    else -> "en"
                }

                sharedPreferences.edit().putString("app_lang", selectedLang).apply()

                val context = LocaleHelper.setLocale(requireContext(), selectedLang)
                val intent = Intent(context, requireActivity()::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                requireActivity().finish()
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

