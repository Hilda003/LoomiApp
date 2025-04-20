package com.example.loomi

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.loomi.utils.LocaleHelper
import dagger.hilt.android.HiltAndroidApp
import android.content.Context

// MyApp.kt
@HiltAndroidApp
class MyApp : Application() {
//    override fun onCreate() {
//        super.onCreate()
//        val prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
//        val isNightMode = prefs.getBoolean("night_mode", false)
//        val lang = prefs.getString("app_lang", "en") ?: "en"
//
//        AppCompatDelegate.setDefaultNightMode(
//            if (isNightMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
//        )
//
//        LocaleHelper.setLocale(this, lang)
//    }
}
