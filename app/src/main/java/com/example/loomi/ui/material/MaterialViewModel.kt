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

class MaterialViewModel : ViewModel(){
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

                    snapshot.documents.forEach { doc ->
                        val material = doc.toObject(Material::class.java)?.copy(id = doc.id)
                        material?.let { mat ->
                            fetchSections(doc.id) { sections ->
                                materialList.add(mat.copy(sections = sections))
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
                val tasks = sectionSnap.map { sectionDoc ->
                    val section = sectionDoc.toObject(Section::class.java)
                    val sectionId = sectionDoc.id

                    db.collection("materials")
                        .document(materialId)
                        .collection("sections")
                        .document(sectionId)
                        .collection("content")
                        .get()
                        .continueWith { contentSnap ->
                            val contentList = contentSnap.result?.documents?.mapNotNull { contentDoc ->
                                contentDoc.toObject(Content::class.java)
                            } ?: emptyList()

                            section.copy(
                                id = sectionId.toIntOrNull() ?: 0,
                                content = contentList
                            )
                        }
                }
                Tasks.whenAllSuccess<Section>(tasks)
                    .addOnSuccessListener { sectionList ->
                        callback(sectionList)
                    }
                    .addOnFailureListener {
                        Log.e("MaterialViewModel", "Failed to fetch sections with content", it)
                        callback(emptyList())
                    }
            }
    }

}