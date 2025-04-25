package com.example.loomi.ui.home

import android.R.attr.text
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.loomi.R
import com.example.loomi.ui.article.ActivityArticle
import com.example.loomi.databinding.FragmentHomeBinding
import com.example.loomi.data.model.Course
import com.example.loomi.data.retrofit.ApiConfig
import com.example.loomi.databinding.ItemMaterialBinding
import com.example.loomi.ui.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.jvm.java

class HomeFragment : Fragment() {


    private var _binding: FragmentHomeBinding? = null
    private lateinit var auth: FirebaseAuth
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        displayUserInfo()
        return binding.root
        ItemMaterialBinding.inflate(inflater, container, false)
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
        binding.itemCourse.tvCompletion.text = course.instructor

        val apiService = ApiConfig.getApiService()

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val articles = withContext(Dispatchers.IO) {
                    apiService.getArticles()
                }

                if (!isAdded) return@launch

                val latestArticle = articles.firstOrNull()
                latestArticle?.let { article ->
                    binding.itemArticle.tvTitleArticle.text = article.title
                    Glide.with(this@HomeFragment)
                        .load(article.coverImage)
                        .into(binding.itemArticle.courseImg)

                    binding.tvSeeAll.setOnClickListener {
                        val intent = Intent(requireContext(), ActivityArticle::class.java)
                        startActivity(intent)
                    }
                }
            } catch (e: Exception) {
                if (isAdded) {
                    Toast.makeText(
                        requireContext(),
                        "Gagal load artikel: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
    private fun displayUserInfo() {
        val user = auth.currentUser
        if (user != null) {
            binding.txtName.text = user.displayName ?: "No Name"
        } else {
//            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }
}