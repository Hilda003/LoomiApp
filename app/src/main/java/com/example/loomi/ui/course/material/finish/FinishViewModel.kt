
package com.example.loomi.ui.course.material.finish

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.loomi.data.model.Section
import com.example.loomi.data.model.UserProgress
import com.example.loomi.ui.home.UserProgressViewModel
import com.example.loomi.utils.ProgressManager
import com.google.firebase.firestore.FirebaseFirestore

class FinishViewModel(application: Application) : AndroidViewModel(application) {
    private val firestore = FirebaseFirestore.getInstance()
    private val _sectionData = MutableLiveData<Pair<Section?, Section?>>()
    val sectionData: LiveData<Pair<Section?, Section?>> = _sectionData
    private val userProgressViewModel = UserProgressViewModel()

    private val _contentToLoad = MutableLiveData<Section>()
    val contentToLoad: LiveData<Section> = _contentToLoad

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _progressUpdated = MutableLiveData<List<UserProgress>>()
    val progressUpdated: LiveData<List<UserProgress>> = _progressUpdated

    private var allSections: List<Section> = emptyList()

    fun markCurrentSectionCompleted(userId: String, materialId: String, currentSectionId: String) {
        ProgressManager.markSectionCompleted(userId, materialId, currentSectionId)
    }

    fun fetchSections(materialId: String, currentSectionId: String, userId: String) {
        firestore.collection("materials")
            .document(materialId)
            .collection("sections")
            .orderBy("order")
            .get()
            .addOnSuccessListener { snapshot ->
                val sections = snapshot.documents.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null
                    val order = when (val raw = data["order"]) {
                        is Long -> raw.toInt()
                        is Double -> raw.toInt()
                        is String -> raw.toIntOrNull() ?: 0
                        else -> 0
                    }

                    Section(
                        docId = doc.id,
                        order = order,
                        title = data["title"] as? String ?: "",
                        content = emptyList()
                    )
                }

                allSections = sections.sortedBy { it.order }

                val currentIndex = allSections.indexOfFirst { it.docId == currentSectionId }
                val current = allSections.getOrNull(currentIndex)
                val next = allSections.getOrNull(currentIndex + 1)

                _sectionData.value = Pair(current, next)

                if (next != null) {
                    unlockNextSection(next, materialId, userId)
                }

            }.addOnFailureListener {
                _error.value = "Failed to fetch sections"
            }
    }

    private fun unlockNextSection(next: Section, materialId: String, userId: String) {
        val nextProgressRef = firestore.collection("user_progress")
            .document(userId)
            .collection("materials")
            .document(materialId)
            .collection("sections")
            .document(next.docId)

        nextProgressRef.get().addOnSuccessListener { doc ->
            val alreadyExists = doc.exists()
            val isUnlocked = doc.getBoolean("isLocked") == false

            if (!alreadyExists || !isUnlocked) {
                val nextProgress = UserProgress(
                    materialId = materialId,
                    sectionId = next.docId,
                    progress = 0,
                    isCompleted = false,
                    isLocked = false
                )
                userProgressViewModel.saveProgress(userId, nextProgress) {
                    ProgressManager.fetchUserProgress(userId, materialId) {
                        _progressUpdated.value = it
                    }
                }
            }
        }.addOnFailureListener {
            _error.value = "Failed unlock next section"
        }
    }

    fun loadContent(section: Section) {
        _contentToLoad.value = section
    }
}
