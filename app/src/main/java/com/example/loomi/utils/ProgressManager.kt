package com.example.loomi.utils



import android.content.Context
import android.content.SharedPreferences

object ProgressManager {
    private const val PREF_NAME = "progress_pref"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun isSectionCompleted(context: Context, sectionId: Int): Boolean {
        return getPrefs(context).getBoolean("SECTION_${sectionId}_COMPLETED", false)
    }

    fun markSectionCompleted(context: Context, sectionId: Int) {
        getPrefs(context).edit().putBoolean("SECTION_${sectionId}_COMPLETED", true).apply()
    }

    fun isSectionUnlocked(context: Context, sectionId: Int): Boolean {
        return sectionId == 1 || isSectionCompleted(context, sectionId - 1)
    }
}
