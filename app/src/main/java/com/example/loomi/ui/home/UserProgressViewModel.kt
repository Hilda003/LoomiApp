package com.example.loomi.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loomi.data.model.UserProgress
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserProgressViewModel : ViewModel() {

    private val _userProgress = MutableLiveData<List<UserProgress>>()
    val userProgress: LiveData<List<UserProgress>> get() = _userProgress

    private val db = FirebaseFirestore.getInstance()

    companion object {
        private const val TAG = "UserProgressViewModel"
    }

    fun fetchUserProgress(userId: String) {
        val db = FirebaseFirestore.getInstance()
        val userProgressRef = db
            .collection("user_progress")
            .document(userId)
            .collection("materials")

        userProgressRef.get().addOnSuccessListener { materialsSnapshot ->
            val progressList = mutableListOf<UserProgress>()

            if (materialsSnapshot.isEmpty) {
                _userProgress.value = progressList
                return@addOnSuccessListener
            }

            val totalMaterials = materialsSnapshot.size()
            var materialsProcessed = 0

            for (materialDoc in materialsSnapshot.documents) {
                val materialId = materialDoc.id
                val sectionsRef = userProgressRef.document(materialId).collection("sections")

                sectionsRef.get().addOnSuccessListener { sectionSnapshot ->
                    for (sectionDoc in sectionSnapshot.documents) {
                        val data = sectionDoc.data
                        if (data != null) {
                            val progress = (data["progress"] as? Long)?.toInt() ?: 0
                            val isCompleted = data["isCompleted"] as? Boolean == true
                            val isLocked = data["isLocked"] as? Boolean != false
                            val sectionId = data["sectionId"] as? String ?: sectionDoc.id

                            progressList.add(
                                UserProgress(
                                    materialId = materialId,
                                    sectionId = sectionId,
                                    progress = progress,
                                    isCompleted = isCompleted,
                                    isLocked = isLocked
                                )
                            )
                        }
                    }

                    materialsProcessed++
                    if (materialsProcessed == totalMaterials) {
                        _userProgress.value = progressList
                    }
                }.addOnFailureListener { error ->

                    materialsProcessed++
                    if (materialsProcessed == totalMaterials) {
                        _userProgress.value = progressList
                    }
                }
            }
        }.addOnFailureListener { error ->
            _userProgress.value = emptyList()
        }
    }


    fun saveProgress(userId: String, progress: UserProgress, onComplete: (() -> Unit)? = null) {
        val docRef = db.collection("user_progress")
            .document(userId)
            .collection("materials")
            .document(progress.materialId)
            .collection("sections")
            .document(progress.sectionId)

        val data = mapOf(
            "progress" to progress.progress,
            "isCompleted" to progress.isCompleted,
            "isLocked" to progress.isLocked
        )

        docRef.set(data)
            .addOnSuccessListener {
                onComplete?.let {
                    viewModelScope.launch(Dispatchers.Main) {
                        it()
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error saving progress", e)
            }
    }
}
