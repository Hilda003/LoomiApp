package com.example.loomi.ui.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.loomi.ContentActivity
import com.example.loomi.R
import com.example.loomi.data.model.Section
import com.example.loomi.databinding.ItemSectionBinding
import com.example.loomi.utils.ProgressManager
import kotlin.jvm.java

class DetailMaterialAdapter(
    private val materialId: String,
    private val sections: List<Section>,
) : RecyclerView.Adapter<DetailMaterialAdapter.SectionViewHolder>() {

    private val sortedSection = sections.sortedBy { it.id }

    inner class SectionViewHolder(val binding: ItemSectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(section: Section) {
            val context = binding.root.context
            val isUnlocked = ProgressManager.isSectionUnlocked(context, section.id)
            section.isLocked = !isUnlocked

            binding.tvNumber.text = section.id.toString()
            binding.tvMaterialTitle.text = section.title
            binding.icLock.setImageResource(
                if (section.isLocked) R.drawable.ic_lock else R.drawable.ic_unlock
            )

            binding.root.setOnClickListener {
                if (!section.isLocked && section.content.isNotEmpty()) {
                    val intent = Intent(context, ContentActivity::class.java)
                    intent.putParcelableArrayListExtra("CONTENT_DATA", ArrayList(section.content))
                    intent.putExtra("SECTION_ID", section.id)
                    intent.putExtra("MATERIAL_ID", materialId)
                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, "Selesaikan materi terlebih dahulu", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val binding = ItemSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        holder.bind(sortedSection[position])
    }

    override fun getItemCount(): Int = sections.size
}

