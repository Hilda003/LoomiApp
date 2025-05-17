package com.example.loomi.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loomi.R
import com.example.loomi.data.model.Material
import com.example.loomi.data.model.UserProgress
import com.example.loomi.databinding.ItemMaterialBinding

class MaterialAdapter(
    private var materials: List<Material>,
    private val onItemClick: (Material) -> Unit,
    private var userProgress: List<UserProgress>
) : RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder>() {


    private var progressMap: Map<String, List<UserProgress>> = emptyMap()

    init {
        groupProgressByMaterial()
    }

    inner class MaterialViewHolder(private val binding: ItemMaterialBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: Material) {
            binding.tvCourseTitle.text = item.title

            Glide.with(binding.root.context)
                .load(item.imgResId)
                .placeholder(R.drawable.article1)
                .into(binding.courseImg)

            val progressForMaterial = progressMap[item.docId].orEmpty()
            val completedCount = progressForMaterial.count { it.isCompleted }
            val totalCount = item.sections.size.takeIf { it > 0 } ?: progressForMaterial.size
            if(totalCount == 0) {
                binding.tvCompletion.text = "Materi belum tersedia"
            } else {
                binding.tvCompletion.text = "$completedCount/$totalCount Selesai"
            }

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

    fun updateData(newMaterials: List<Material>) {
        this.materials = newMaterials
        notifyDataSetChanged()
    }

    fun updateUserProgress(newProgress: List<UserProgress>) {
        this.userProgress = newProgress
        groupProgressByMaterial()
        notifyDataSetChanged()
    }

    fun getUserProgressForMaterial(materialId: String): List<UserProgress> {
        return progressMap[materialId].orEmpty()
    }

    private fun groupProgressByMaterial() {
        progressMap = userProgress.groupBy { it.materialId }
    }
}
