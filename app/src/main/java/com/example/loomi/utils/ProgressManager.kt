package com.example.loomi.utils


import android.annotation.SuppressLint
import android.util.Log
import com.example.loomi.data.model.UserProgress
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

object ProgressManager {
    private const val TAG = "FirestoreProgress"
    @SuppressLint("StaticFieldLeak")
    private val firestore = FirebaseFirestore.getInstance()

    fun markSectionCompleted(userId: String, materialId: String, sectionId: String) {
        if (userId.isEmpty() || materialId.isEmpty() || sectionId.isEmpty()) {
            Log.e(TAG, "Cannot mark section completed: empty parameters")
            return
        }

        val progressRef = firestore.collection("user_progress")
            .document(userId)
            .collection("materials")
            .document(materialId)
            .collection("sections")
            .document(sectionId)

        val progressData = mapOf(
            "isCompleted" to true,
            "isLocked" to false,
            "progress" to 100,
            "userId" to userId,
            "materialId" to materialId,
            "sectionId" to sectionId,
            "lastUpdated" to System.currentTimeMillis()
        )
        progressRef.set(progressData, SetOptions.merge())
            .addOnSuccessListener {
                Log.d(TAG, "Section $sectionId marked completed")

                unlockNextSection(userId, materialId, sectionId)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error updating section completion", e)
            }
    }

    private fun unlockNextSection(userId: String, materialId: String, currentSectionId: String) {
        firestore.collection("sections")
            .whereEqualTo("materialId", materialId)
            .orderBy("order")
            .get()
            .addOnSuccessListener { sectionSnapshot ->
                val sortedSections = sectionSnapshot.documents.mapNotNull { doc ->
                    try {
                        val order = (doc.getLong("order") ?: 0).toInt()
                        val id = doc.id

                        Pair(id, order)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error parsing section", e)
                        null
                    }
                }.sortedBy { it.second }

                val currentIndex = sortedSections.indexOfFirst { it.first == currentSectionId }
                if (currentIndex == -1 || currentIndex >= sortedSections.size - 1) {
                    Log.d(TAG, "No next section to unlock")
                    return@addOnSuccessListener
                }

                val nextSectionId = sortedSections[currentIndex + 1].first

                val nextSectionRef = firestore.collection("user_progress")
                    .document(userId)
                    .collection("materials")
                    .document(materialId)
                    .collection("sections")
                    .document(nextSectionId)

                val unlockData = mapOf(
                    "isLocked" to false,
                    "userId" to userId,
                    "materialId" to materialId,
                    "sectionId" to nextSectionId,
                    "lastUpdated" to System.currentTimeMillis()
                )

                nextSectionRef.set(unlockData, SetOptions.merge())
                    .addOnSuccessListener {
                        Log.d(TAG, "Next section $nextSectionId unlocked")
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error unlocking next section", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error getting sections for unlocking", e)
            }
    }
    fun fetchUserProgress(userId: String, materialId: String, callback: (List<UserProgress>) -> Unit) {
        if (userId.isEmpty() || materialId.isEmpty()) {
            Log.e(TAG, "Cannot fetch progress: empty parameters")
            callback(emptyList())
            return
        }

        firestore.collection("user_progress")
            .document(userId)
            .collection("materials")
            .document(materialId)
            .collection("sections")
            .get()
            .addOnSuccessListener { snapshot ->
                val progress = snapshot.documents.mapNotNull { doc ->
                    try {
                        val data = doc.data ?: return@mapNotNull null
                        val isCompleted = data["isCompleted"] as? Boolean == true
                        val isLocked = data["isLocked"] as? Boolean != false
                        val progressValue = when (val rawProgress = data["progress"]) {
                            is Long -> rawProgress.toInt()
                            is Double -> rawProgress.toInt()
                            is String -> rawProgress.toIntOrNull() ?: 0
                            else -> 0
                        }

                        UserProgress(
                            sectionId = doc.id,
                            materialId = materialId,
                            progress = progressValue,
                            isCompleted = isCompleted,
                            isLocked = isLocked
                        )
                    } catch (e: Exception) {
                        Log.e(TAG, "Error parsing progress document", e)
                        null
                    }
                }

                Log.d(TAG, "Fetched ${progress.size} progress items")

                if (progress.isEmpty()) {
                    initializeFirstSectionProgress(userId, materialId) {
                        fetchUserProgress(userId, materialId, callback)
                    }
                } else {
                    callback(progress)
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error fetching progress", e)
                callback(emptyList())
            }
    }

    private fun initializeFirstSectionProgress(userId: String, materialId: String, onComplete: () -> Unit) {
        firestore.collection("sections")
            .whereEqualTo("materialId", materialId)
            .orderBy("order")
            .limit(1)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.documents.isNotEmpty()) {
                    val firstSectionId = querySnapshot.documents[0].id

                    val progressRef = firestore.collection("user_progress")
                        .document(userId)
                        .collection("materials")
                        .document(materialId)
                        .collection("sections")
                        .document(firstSectionId)

                    val initialData = mapOf(
                        "isLocked" to false,
                        "isCompleted" to false,
                        "progress" to 0,
                        "userId" to userId,
                        "materialId" to materialId,
                        "sectionId" to firstSectionId,
                        "lastUpdated" to System.currentTimeMillis()
                    )

                    progressRef.set(initialData, SetOptions.merge())
                        .addOnSuccessListener {
                            Log.d(TAG, "First section $firstSectionId initialized")
                            onComplete()
                        }
                        .addOnFailureListener { e ->
                            Log.e(TAG, "Error initializing first section", e)
                            onComplete()
                        }
                } else {
                    Log.w(TAG, "No sections found for this material")
                    onComplete()
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error finding first section", e)
                onComplete()
            }
    }
    internal suspend fun fetchMaterialProgressSummary(
        userId: String,
        materialId: String
    ): Pair<Int, Int> {
        return try {
            val sectionsSnapshot = firestore.collection("materials")
                .document(materialId)
                .collection("sections")
                .get()
                .await()

            val totalSections = sectionsSnapshot.size()
            if (totalSections == 0) return Pair(0, 0)

            val progressSnapshot = firestore.collection("user_progress")
                .document(userId)
                .collection("materials")
                .document(materialId)
                .collection("sections")
                .get()
                .await()

            val completedCount = progressSnapshot.documents.count {
                it.getBoolean("isCompleted") == true
            }

            Pair(completedCount, totalSections)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch progress for $materialId", e)
            Pair(0, 0)
        }
    }




    fun checkSectionLockStatus(userId: String, materialId: String, sectionId: String, callback: (Boolean) -> Unit) {
        if (userId.isEmpty() || materialId.isEmpty() || sectionId.isEmpty()) {
            Log.e(TAG, "Cannot check lock status: empty parameters")
            callback(true)
            return
        }
        firestore.collection("sections")
            .whereEqualTo("materialId", materialId)
            .get()
            .addOnSuccessListener { sectionsSnapshot ->

                val orderedSections = sectionsSnapshot.documents
                    .mapNotNull { doc ->
                        try {
                            val order = (doc.getLong("order") ?: Long.MAX_VALUE).toInt()
                            Pair(doc.id, order)
                        } catch (_: Exception) {
                            null
                        }
                    }
                    .sortedBy { it.second }

                val sectionIds = orderedSections.map { it.first }
                val position = sectionIds.indexOf(sectionId)

                if (position == 0) {
                    Log.d(TAG, "First section, automatically unlocked")

                    val firstSectionRef = firestore.collection("user_progress")
                        .document(userId)
                        .collection("materials")
                        .document(materialId)
                        .collection("sections")
                        .document(sectionId)

                    val initialData = mapOf(
                        "isLocked" to false,
                        "isCompleted" to false,
                        "progress" to 0,
                        "userId" to userId,
                        "materialId" to materialId,
                        "sectionId" to sectionId
                    )

                    firstSectionRef.set(initialData, SetOptions.merge())
                        .addOnSuccessListener {
                            callback(false)
                        }
                        .addOnFailureListener { e ->
                            Log.e(TAG, "Error creating first section progress", e)
                            callback(false)
                        }

                    return@addOnSuccessListener
                }

                val previousSectionId = sectionIds.getOrNull(position - 1)
                if (previousSectionId == null) {
                    Log.e(TAG, "Cannot find previous section for $sectionId")
                    callback(true)
                    return@addOnSuccessListener
                }
                firestore.collection("user_progress")
                    .document(userId)
                    .collection("materials")
                    .document(materialId)
                    .collection("sections")
                    .document(previousSectionId)
                    .get()
                    .addOnSuccessListener { previousDoc ->
                        val isPreviousCompleted = if (previousDoc.exists()) {
                            previousDoc.getBoolean("isCompleted") == true
                        } else {
                            false
                        }

                        Log.d(TAG, "Previous section $previousSectionId completed: $isPreviousCompleted")
                        val shouldBeLocked = !isPreviousCompleted
                        val currentSectionRef = firestore.collection("user_progress")
                            .document(userId)
                            .collection("materials")
                            .document(materialId)
                            .collection("sections")
                            .document(sectionId)
                        val lockData = mapOf(
                            "isLocked" to shouldBeLocked,
                            "userId" to userId,
                            "materialId" to materialId,
                            "sectionId" to sectionId
                        )

                        currentSectionRef.set(lockData, SetOptions.merge())
                            .addOnSuccessListener {
                                Log.d(TAG, "Updated lock status for $sectionId to $shouldBeLocked")
                                callback(shouldBeLocked)
                            }
                            .addOnFailureListener { e ->
                                Log.e(TAG, "Error updating lock status", e)
                                callback(shouldBeLocked)
                            }
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error checking previous section completion", e)
                        callback(true)
                    }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error getting section order", e)
                callback(true)
            }
    }


}