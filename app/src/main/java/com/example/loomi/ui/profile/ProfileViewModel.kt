package com.example.loomi.ui.profile


import android.app.Application
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.example.loomi.R
import com.example.loomi.data.prefs.UserPreferences
import com.example.loomi.utils.ImageUtil
import com.example.loomi.utils.NotificationHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ProfileViewModel(
    private val app: Application,
    private val prefs: UserPreferences,
    private val userId: String
) : AndroidViewModel(app) {

    private val _user = MutableLiveData<FirebaseUser>()
    val user: LiveData<FirebaseUser> = _user

    private val _notifEnabled = MutableLiveData<Boolean>()
    val notifEnabled: LiveData<Boolean> = _notifEnabled

    private val _language = MutableLiveData<String>()
    val language: LiveData<String> = _language

    init {
        _notifEnabled.value = prefs.isNotifEnabled(userId)
        _language.value = prefs.getLanguage()
    }

    fun fetchUser() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.reload()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _user.value = currentUser
            }
        }
    }

    fun loadProfileImage(fragment: Fragment, user: FirebaseUser, imageView: ImageView) {
        val photoUrl = user.photoUrl
        if (photoUrl != null) {
            Glide.with(fragment)
                .load(photoUrl)
                .placeholder(R.drawable.circle_background)
                .into(imageView)
        } else {
            val initial = user.email.orEmpty().firstOrNull()?.uppercase() ?: "?"
            imageView.setImageBitmap(ImageUtil.generateInitialBitmap(initial))
        }
    }

    fun toggleNotification(enabled: Boolean) {
        prefs.setNotifEnabled(userId, enabled)
        _notifEnabled.value = enabled
        if (!enabled) NotificationHelper.cancelNotification(app)
    }

    fun setNotificationTime(hour: Int, minute: Int) {
        prefs.setNotifTime(userId, hour, minute)
        NotificationHelper.setDailyNotification(app, hour, minute)
    }

    fun setLanguage(lang: String) {
        prefs.setLanguage(lang)
        _language.value = lang
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }
}

