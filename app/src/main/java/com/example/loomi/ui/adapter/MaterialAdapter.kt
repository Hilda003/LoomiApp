package com.example.loomi.ui.adapter

import android.R.attr.text
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loomi.databinding.ItemCourseHeaderBinding
import com.example.loomi.model.Material
import com.example.loomi.model.Section


class MaterialAdapter(
    private val materials: List<Material>,
    private val onSectionClickCallback: (Section) -> Unit
) : RecyclerView.Adapter<MaterialAdapter.CourseViewHolder>() {

    private val expandedPositionSet = mutableSetOf<Int>()

    inner class CourseViewHolder(val binding: ItemCourseHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(material: Material, position: Int) {
            val isExpanded = expandedPositionSet.contains(position)

            binding.tvMaterialTitle.text = material.title

            binding.ivArrow.setImageDrawable(
                if (isExpanded)
                    binding.root.context.getDrawable(com.example.loomi.R.drawable.ic_arrow_up)
                else
                    binding.root.context.getDrawable(com.example.loomi.R.drawable.ic_arrow_down)
            )
            val moduleAdapter = SectionAdapter(
                sectionList = material.sections,
                onSectionClick = onSectionClickCallback
            )


            binding.rvModules.apply {
                layoutManager = LinearLayoutManager(binding.root.context)
                adapter = moduleAdapter
                visibility = if (isExpanded) View.VISIBLE else View.GONE
            }

            binding.layoutHeader.setOnClickListener {
                if (isExpanded) {
                    expandedPositionSet.remove(position)
                } else {
                    expandedPositionSet.add(position)
                }
                notifyItemChanged(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCourseHeaderBinding.inflate(inflater, parent, false)
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bind(materials[position], position)
    }

    override fun getItemCount(): Int = materials.size
}
