package com.example.loomi.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.loomi.databinding.ItemModuleBinding
import com.example.loomi.model.Section

class SectionAdapter(
    private var sectionList: List<Section>,
    private val onSectionClick: (Section) -> Unit
) : RecyclerView.Adapter<SectionAdapter.MaterialViewHolder>() {

    inner class MaterialViewHolder(val binding: ItemModuleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(section: Section) {
            binding.tvSectionTitle.text = section.title
            binding.root.setOnClickListener {
                onSectionClick(section)
                Toast.makeText(
                    binding.root.context,
                    "Clicked on: ${section.title}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemModuleBinding.inflate(inflater, parent, false)
        return MaterialViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MaterialViewHolder, position: Int) {
        holder.bind(sectionList[position])
    }

    override fun getItemCount(): Int = sectionList.size

    fun updateSections(newSections: List<Section>) {
        sectionList = newSections
        notifyDataSetChanged()
    }
}

