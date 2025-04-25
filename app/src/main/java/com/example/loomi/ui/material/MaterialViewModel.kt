package com.example.loomi.ui.material

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.loomi.data.model.Content
import com.example.loomi.data.model.ContentType
import com.example.loomi.data.model.Material
import com.example.loomi.data.model.Section
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore

class MaterialViewModel : ViewModel() {
    private val _materials = MutableLiveData<List<Material>>()
    val materials: LiveData<List<Material>> get() = _materials

    private val db = FirebaseFirestore.getInstance()

    init {
        fetchMaterials()
    }

    private fun fetchMaterials() {
        db.collection("materials")
            .get()
            .addOnSuccessListener { snapshot ->
                val materialList = mutableListOf<Material>()
                val totalTasks = snapshot.documents.size
                var completedTasks = 0

                if (totalTasks == 0) {
                    _materials.value = emptyList()
                    return@addOnSuccessListener
                }

                snapshot.documents.forEach { doc ->
                    val baseMaterial = doc.toObject(Material::class.java)?.copy(id = doc.id)

                    if (baseMaterial != null) {
                        fetchSections(doc.id) { sections ->
                            val fullMaterial = baseMaterial.copy(sections = sections)
                            materialList.add(fullMaterial)

                            completedTasks++
                            if (completedTasks == totalTasks) {
                                _materials.value = materialList
                            }
                        }
                    } else {
                        completedTasks++
                        if (completedTasks == totalTasks) {
                            _materials.value = materialList
                        }
                    }
                }
            }
            .addOnFailureListener {
                Log.e("MaterialViewModel", "Failed to load materials", it)
            }
    }

    private fun fetchSections(materialId: String, callback: (List<Section>) -> Unit) {
        db.collection("materials")
            .document(materialId)
            .collection("sections")
            .get()
            .addOnSuccessListener { sectionSnap ->
                val sectionTasks = sectionSnap.map { sectionDoc ->
                    val raw = sectionDoc.data
                    val id = (raw["id"] as? Long)?.toInt() ?: 0
                    val title = raw["title"] as? String ?: ""
                    val isLocked = raw["isLocked"] as? Boolean ?: true

                    db.collection("materials")
                        .document(materialId)
                        .collection("sections")
                        .document(sectionDoc.id)
                        .collection("content")
                        .get()
                        .continueWith { contentSnap ->
                            val contentList = contentSnap.result?.documents?.mapNotNull { contentDoc ->
                                parseContent(contentDoc.data ?: return@mapNotNull null)
                            } ?: emptyList()

                            Section(
                                id = id,
                                title = title,
                                isLocked = isLocked,
                                content = contentList
                            )
                        }
                }

                Tasks.whenAllSuccess<Section>(sectionTasks)
                    .addOnSuccessListener { sectionList ->
                        callback(sectionList)
                    }
                    .addOnFailureListener {
                        Log.e("MaterialViewModel", "Failed to fetch sections with content", it)
                        callback(emptyList())
                    }
            }
            .addOnFailureListener {
                Log.e("MaterialViewModel", "Failed to load sections", it)
                callback(emptyList())
            }
    }

    private fun parseContent(raw: Map<String, Any>): Content? {
        val typeStr = raw["type"] as? String ?: return null
        val type = ContentType.fromString(typeStr)
        val title = raw["title"] as? String ?: ""

        Log.d("ParseContent", "Raw content: $raw")

        return when (type) {
            ContentType.EXPLANATION -> {
                val dataRaw = raw["data"]
                val description = when (dataRaw) {
                    is List<*> -> dataRaw.joinToString("\n") { it.toString() } // Menggabungkan list jadi satu string
                    is Map<*, *> -> dataRaw["text"] as? String ?: ""
                    else -> ""
                }

                Log.d("ParseContent", "Explanation content parsed: title=$title, description=$description")

                Content(
                    type = type,
                    title = title,
                    description = description
                )
            }

            ContentType.FILL_IN_THE_BLANK -> {
                val dataMap = raw["data"] as? Map<*, *> ?: return null

                Content(
                    type = type,
                    title = title,
                    description = dataMap["text"] as? String ?: "",
                    code = dataMap["code"] as? String,
                    correctAnswer = (dataMap["correctAnswer"] as? List<*>)?.joinToString()
                )
            }

            ContentType.MULTIPLE_CHOICE -> {
                val dataMap = raw["data"] as? Map<*, *> ?: return null

                Content(
                    type = type,
                    title = title,
                    question = dataMap["question"] as? String,
                    code = dataMap["code"] as? String,
                    choices = dataMap["choices"] as? List<String>,
                    correctAnswer = raw["correctAnswer"] as? String
                )
            }

            ContentType.DRAG_AND_DROP -> {
                val dataMap = raw["data"] as? Map<*, *> ?: return null
                Content(
                    type = type,
                    title = title,
                    description = dataMap["text"] as? String ?: "",
                    choices = dataMap["drag"] as? List<String>,
                    correctAnswer = (dataMap["correctAnswer"] as? List<*>)?.joinToString()
                )
            }
        }
    }


}
