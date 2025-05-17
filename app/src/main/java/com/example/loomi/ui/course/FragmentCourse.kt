package com.example.loomi.ui.course

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.loomi.R
import com.example.loomi.databinding.FragmentCourseBinding

import com.example.loomi.ui.adapter.CoursePagerAdapter
import com.google.android.material.tabs.TabLayoutMediator


class FragmentCourse : Fragment() {

    private var _binding: FragmentCourseBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CoursePagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = CoursePagerAdapter(this)
        binding.viewPager.adapter = adapter

        val font = ResourcesCompat.getFont(requireContext(), R.font.poppins_semibold)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Kelas"
                1 -> "Deskripsi"
                else -> null
            }
        }.attach()

        for (i in 0 until binding.tabLayout.tabCount) {
            val tab = binding.tabLayout.getTabAt(i)
            val tabTextView = TextView(requireContext()).apply {
                text = tab?.text
                setTextColor(ContextCompat.getColor(context, R.color.white))
                typeface = font
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            tab?.customView = tabTextView
        }
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
