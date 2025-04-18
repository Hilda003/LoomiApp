package com.example.loomi.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.loomi.databinding.FragmentHomeBinding
import com.example.loomi.databinding.ItemCourseBinding
import com.example.loomi.model.Course

class HomeFragment : Fragment() {


    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
        ItemCourseBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundColor(Color.WHITE)
        val course = Course(
            title = "Algoritma Dasar",
            instructor = "Ronal",
            lessonCount = 9,
            progress = 87,
        )
        binding.itemCourse.tvCourseTitle.text = course.title
        binding.itemCourse.tvInstructor.text = course.instructor
        binding.itemCourse.progressBar.progress = course.progress
    }
}