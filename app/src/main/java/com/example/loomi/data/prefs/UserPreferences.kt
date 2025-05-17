package com.example.loomi.data.prefs


import android.content.Context


class UserPreferences(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    fun isNotifEnabled(userId: String): Boolean {
        return sharedPreferences.getBoolean("${userId}_notif_enabled", false)
    }

    fun setNotifEnabled(userId: String, enabled: Boolean) {
        sharedPreferences.edit().putBoolean("${userId}_notif_enabled", enabled).apply()
    }

    fun setNotifTime(userId: String, hour: Int, minute: Int) {
        sharedPreferences.edit().apply {
            putInt("${userId}_notif_hour", hour)
            putInt("${userId}_notif_minute", minute)
            apply()
        }
    }

    fun getLanguage(): String {
        return sharedPreferences.getString("app_lang", "en") ?: "en"
    }

    fun setLanguage(lang: String) {
        sharedPreferences.edit().putString("app_lang", lang).apply()
    }
}
