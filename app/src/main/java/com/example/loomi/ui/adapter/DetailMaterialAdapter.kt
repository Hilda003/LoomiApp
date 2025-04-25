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
import kotlin.jvm.java

class DetailMaterialAdapter(
    private val sections: List<Section>,
) : RecyclerView.Adapter<DetailMaterialAdapter.SectionViewHolder>() {

    inner class SectionViewHolder(val binding: ItemSectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(section: Section) {
            binding.tvNumber.text = section.id.toString()
            binding.tvMaterialTitle.text = section.title
            binding.icLock.setImageResource(
                if (section.isLocked) R.drawable.ic_lock else R.drawable.ic_unlock
            )
            binding.root.setOnClickListener {
                if (section.isLocked && section.content.isNotEmpty()) {
                    val context = binding.root.context
                    val intent = Intent(context, ContentActivity::class.java)
                    intent.putParcelableArrayListExtra("CONTENT_DATA", ArrayList(section.content))
                    context.startActivity(intent)
                } else if (section.isLocked && section.content.isEmpty()) {
                    Toast.makeText(binding.root.context, "Konten belum tersedia", Toast.LENGTH_SHORT).show()
                }

            }
            Log.d("AdapterCheck", "Section ID: ${section.id}, Title: ${section.title}")


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val binding = ItemSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        holder.bind(sections[position])
    }

    override fun getItemCount(): Int = sections.size
}
