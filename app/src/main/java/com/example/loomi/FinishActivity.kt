package com.example.loomi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loomi.data.model.Content
import com.example.loomi.data.model.Section
import com.example.loomi.databinding.ActivityFinishBinding
import com.example.loomi.utils.ContentParser
import com.google.firebase.firestore.FirebaseFirestore

class FinishActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFinishBinding
    private val firestore = FirebaseFirestore.getInstance()
    private var allSections: List<Section> = emptyList()
    private lateinit var materialId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionId = intent.getStringExtra("SECTION_ID") ?: ""
        materialId = intent.getStringExtra("MATERIAL_ID") ?: ""

//        if (materialId.isEmpty() || sectionId.isEmpty()) {
//            Toast.makeText(this, "Data materi tidak valid", Toast.LENGTH_SHORT).show()
//            finish()
//            return
//        }
        getSectionsFromFirestore(sectionId)

        binding.icClose.setOnClickListener {
            finish()
        }
    }

    private fun getSectionsFromFirestore(currentSectionId: String) {
        val sectionRef = firestore.collection("materials")
            .document(materialId)
            .collection("sections")

        sectionRef.orderBy("order").get()
            .addOnSuccessListener { snapshot ->
                val sections = snapshot.documents.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null
                    val order = when (val rawId = data["order"]) {
                        is Long -> rawId.toInt()
                        is Double -> rawId.toInt()
                        is String -> rawId.toIntOrNull() ?: 0
                        else -> 0
                    }

                    Section(
                        docId = doc.id,
                        order = order,
                        title = data["title"] as? String ?: "",
                        isLocked = data["isLocked"] as? Boolean ?: true,
                        isCompleted = data["isCompleted"] as? Boolean ?: false,
                        content = emptyList()
                    )
                }

                allSections = sections.sortedBy { it.order }

                val currentIndex = allSections.indexOfFirst { it.docId == currentSectionId }
                val currentSection = allSections.getOrNull(currentIndex)
                val nextSection = allSections.getOrNull(currentIndex + 1)

                if (currentSection == null) {
                    Toast.makeText(this, "Materi saat ini tidak ditemukan", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                sectionRef.document(currentSection.docId)
                    .update("isCompleted", true)

                if (nextSection != null) {
                    sectionRef.document(nextSection.docId)
                        .update("isLocked", false)
                    nextSection.isLocked = false
                }

                updateUI(currentSection, nextSection)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal mengambil data dari Firestore", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUI(current: Section?, next: Section?) {
        binding.txtTitleMaterialNow.text = current?.title ?: "-"
        binding.txtNextMaterial.text = next?.title ?: "Tidak ada materi selanjutnya"

        binding.btnNextMaterial.setOnClickListener {
            if (next != null) {
                if (!next.isLocked) {
                    loadContentAndNavigate(next)
                } else {
                    Toast.makeText(this, "Selesaikan materi sebelumnya terlebih dahulu", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Tidak ada materi selanjutnya", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadContentAndNavigate(section: Section) {
        val contentRef = firestore.collection("materials")
            .document(materialId)
            .collection("sections")
            .document(section.docId)
            .collection("content")

        contentRef.get()
            .addOnSuccessListener { snapshot ->
                val contents = snapshot.documents.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null
                    ContentParser.parseContent(data)
                }
                val intent = Intent(this, ContentActivity::class.java).apply {
                    putExtra("SECTION_ID", section.docId)
                    putExtra("MATERIAL_ID", materialId)
                    putParcelableArrayListExtra("CONTENT_DATA", ArrayList(contents))
                }
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal memuat konten", Toast.LENGTH_SHORT).show()
                Log.e("FinishActivity", "Error loading content", it)
            }
    }
}
