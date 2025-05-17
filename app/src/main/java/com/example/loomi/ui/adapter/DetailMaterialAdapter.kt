package com.example.loomi.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.loomi.ui.course.material.content.ContentActivity
import com.example.loomi.R
import com.example.loomi.data.model.Section
import com.example.loomi.data.model.UserProgress
import com.example.loomi.databinding.ItemSectionBinding
import com.example.loomi.utils.ProgressManager

class DetailMaterialAdapter(
    private val materialId: String,
    private val sections: List<Section>,
    private var userProgressList: List<UserProgress>,
    private val userId: String,
) : RecyclerView.Adapter<DetailMaterialAdapter.SectionViewHolder>() {

    private val sortedSections = sections.sortedBy { it.order }

    init {
        if (sortedSections.isNotEmpty()) {
            Log.d("DetailAdapter", "Initializing adapter with ${sortedSections.size} sections")
            ensureFirstSectionUnlocked()
        }
    }

    private fun ensureFirstSectionUnlocked() {
        if (sortedSections.isEmpty()) return

        val firstSection = sortedSections.first()
        val firstSectionProgress = userProgressList.find { it.sectionId == firstSection.docId }

        if (firstSectionProgress == null) {
            ProgressManager.checkSectionLockStatus(userId, materialId, firstSection.docId) { _ ->
                ProgressManager.fetchUserProgress(userId, materialId) { newProgress ->
                    updateUserProgress(newProgress)
                }
            }
        }
    }

    inner class SectionViewHolder(val binding: ItemSectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(section: Section, position: Int) {
            val context = binding.root.context
            val progressMap = userProgressList.associateBy { it.sectionId }
            val currentProgress = progressMap[section.docId]
            currentProgress?.isCompleted == true
            var isLocked = true

            if (position == 0) {
                isLocked = false

            } else {
                val previousSectionId = sortedSections.getOrNull(position - 1)?.docId
                if (previousSectionId != null) {
                    val previousProgress = progressMap[previousSectionId]
                    val isPreviousCompleted = previousProgress?.isCompleted == true
                    isLocked = !isPreviousCompleted
                }
            }
            binding.tvNumber.text = section.order.toString()
            binding.tvMaterialTitle.text = section.title
            binding.icLock.setImageResource(
                if (isLocked) R.drawable.ic_lock else R.drawable.ic_lock_open
            )

            binding.root.setOnClickListener {
                if (!isLocked && section.content.isNotEmpty()) {
                    val intent = Intent(context, ContentActivity::class.java).apply {
                        putParcelableArrayListExtra("CONTENT_DATA", ArrayList(section.content))
                        putExtra("SECTION_ID", section.docId)
                        putExtra("MATERIAL_ID", materialId)
                        putExtra("USER_ID", userId)
                    }
                    context.startActivity(intent)
                    ProgressManager.fetchUserProgress(userId, materialId) { newProgressList ->
                        updateUserProgress(newProgressList)
                    }
                } else {
                    Toast.makeText(context, "Selesaikan materi sebelumnya terlebih dahulu", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val binding = ItemSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        holder.bind(sortedSections[position], position)
    }

    override fun getItemCount(): Int = sortedSections.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateUserProgress(newProgress: List<UserProgress>) {
        this.userProgressList = newProgress
        notifyDataSetChanged()
    }


}