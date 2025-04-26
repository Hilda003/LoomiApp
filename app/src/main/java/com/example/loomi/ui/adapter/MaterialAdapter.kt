package com.example.loomi.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loomi.R
import com.example.loomi.databinding.ItemCourseHeaderBinding
import com.example.loomi.data.model.Material
import com.example.loomi.databinding.ItemMaterialBinding


class MaterialAdapter(
    private val materials: List<Material>,
    private val onItemClick: (Material) -> Unit
) : RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder>() {

    inner class MaterialViewHolder(val binding: ItemMaterialBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Material) {
            binding.tvCourseTitle.text = item.title
            Glide.with(binding.root.context)
                .load(item.imgResId)
                .placeholder(R.drawable.article1)
                .into(binding.courseImg)

            val done = item.sections.count { it.isCompleted }
            val total = item.sections.size
            binding.tvCompletion.text = "$done/$total Selesai"
            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialViewHolder {
        val binding = ItemMaterialBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MaterialViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MaterialViewHolder, position: Int) {
        holder.bind(materials[position])
    }

    override fun getItemCount(): Int = materials.size
}


