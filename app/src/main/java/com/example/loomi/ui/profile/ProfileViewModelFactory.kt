package com.example.loomi.ui.profile

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.loomi.data.prefs.UserPreferences

class ProfileViewModelFactory(
    private val context: Context,
    private val userId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val prefs = UserPreferences(context)
        return ProfileViewModel(context.applicationContext as Application, prefs, userId) as T
    }
}
