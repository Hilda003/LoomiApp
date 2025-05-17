package com.example.loomi.ui.profile

import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.loomi.R
import com.example.loomi.databinding.DialogLanguageBinding
import com.example.loomi.databinding.FragmentProfileBinding
import com.example.loomi.ui.auth.LoginActivity
import com.example.loomi.utils.LocaleHelper
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ProfileViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        val userUid = auth.currentUser?.uid.orEmpty()

        viewModel = ViewModelProvider(this, ProfileViewModelFactory(requireContext(), userUid))
            .get(ProfileViewModel::class.java)

        requestNotificationPermissionIfNeeded()
        observeViewModel()
        setupUI()

        binding.switchNightMode.setOnClickListener {
            Toast.makeText(requireContext(), "Fitur masih dalam tahap pengembangan", Toast.LENGTH_SHORT).show()
        }
        return binding.root

    }

    private fun observeViewModel() {
        viewModel.user.observe(viewLifecycleOwner) { user ->
            binding.txtUsername.text = user.displayName ?: "No Name"
            binding.txtEmail.text = user.email ?: "No Email"
            viewModel.loadProfileImage(this, user, binding.ivProfile)
        }

        viewModel.notifEnabled.observe(viewLifecycleOwner) { enabled ->
            if (binding.switchNotification.isChecked != enabled) {
                binding.switchNotification.setOnCheckedChangeListener(null)
                binding.switchNotification.isChecked = enabled
                binding.switchNotification.setOnCheckedChangeListener(notificationSwitchListener)
            }
        }
    }

    private fun setupUI() {
        viewModel.fetchUser()

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }

        binding.btnLanguage.setOnClickListener {
            showLanguageDialog()
        }
        binding.switchNotification.setOnCheckedChangeListener(notificationSwitchListener)
    }

    private val notificationSwitchListener =
        { _: android.widget.CompoundButton, isChecked: Boolean ->
            viewModel.toggleNotification(isChecked)
            if (isChecked) {
                showTimePickerDialog()
            } else {
                Toast.makeText(requireContext(), "Reminder dimatikan", Toast.LENGTH_SHORT).show()
            }
        }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePicker = TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
            viewModel.setNotificationTime(selectedHour, selectedMinute)
            Toast.makeText(
                requireContext(),
                "Reminder di-set jam $selectedHour:$selectedMinute",
                Toast.LENGTH_SHORT
            ).show()
        }, hour, minute, true)

        timePicker.show()
    }

    private fun showLanguageDialog() {
        val dialogBinding = DialogLanguageBinding.inflate(LayoutInflater.from(requireContext()))
        val currentLang = viewModel.language.value ?: "en"

        when (currentLang) {
            "en" -> dialogBinding.radioEnglish.isChecked = true
            "id" -> dialogBinding.radioIndonesian.isChecked = true
        }

        AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setPositiveButton("OK") { _, _ ->
                val selectedLang = when (dialogBinding.radioGroupLanguage.checkedRadioButtonId) {
                    R.id.radioEnglish -> "en"
                    R.id.radioIndonesian -> "id"
                    else -> "en"
                }
                viewModel.setLanguage(selectedLang)
                val context = LocaleHelper.setLocale(requireContext(), selectedLang)
                val intent = Intent(context, requireActivity()::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                requireActivity().finish()
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
