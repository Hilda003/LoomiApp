package com.example.loomi.utils


import android.content.Context
import android.content.SharedPreferences
import com.example.loomi.data.model.Section
import kotlin.collections.indexOfFirst

object ProgressManager {
    private const val PREF_NAME = "progress_pref"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
    fun isSectionCompleted(context: Context, docId: String): Boolean {
        return getPrefs(context).getBoolean("SECTION_${docId}_COMPLETED", false)
    }

    fun markSectionCompleted(context: Context, docId: String) {
        getPrefs(context).edit().putBoolean("SECTION_${docId}_COMPLETED", true).apply()
    }

    fun isSectionUnlocked(
        context: Context,
        currentSection: Section,
        sortedSections: List<Section>,
        onResult: (Boolean) -> Unit
    ) {
        val index = sortedSections.indexOfFirst { it.docId == currentSection.docId }
        if (index == 0) {

            onResult(true)
        } else {
            val previousSection = sortedSections[index - 1]
            val isCompleted = isSectionCompleted(context, previousSection.docId)
            onResult(isCompleted)
        }
    }

    private fun isPreviousSectionCompleted(context: Context, docId: String, sections: List<String>): Boolean {
        val currentIndex = sections.indexOf(docId)
        return if (currentIndex > 0) {

            isSectionCompleted(context, sections[currentIndex - 1])
        } else {
            false
        }
    }
    private fun getAllCompletedSections(context: Context): List<String> {
        val prefs = getPrefs(context)
        val allKeys = prefs.all.keys.filter { it.contains("_COMPLETED") }

        return allKeys.map { it.replace("SECTION_", "").replace("_COMPLETED", "") }
    }
}

