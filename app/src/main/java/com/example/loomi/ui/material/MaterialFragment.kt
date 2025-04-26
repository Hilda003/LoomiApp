package com.example.loomi.ui.material

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loomi.ui.material.DetailMaterialActivity
import com.example.loomi.databinding.FragmentMaterialBinding
import com.example.loomi.ui.adapter.MaterialAdapter
import kotlin.jvm.java

class MaterialFragment : Fragment() {

    private var _binding: FragmentMaterialBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MaterialViewModel
    private lateinit var adapter: MaterialAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMaterialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("DEBUG", "Package name: ${requireContext().packageName}")

        setupRecyclerView()
        setupObserver()
        setupInsets()
        showLoading(true)
    }

    private fun setupRecyclerView() {
        binding.rvCourse.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            isNestedScrollingEnabled = true
        }
    }

    private fun setupObserver() {
        viewModel = ViewModelProvider(this)[MaterialViewModel::class.java]
        viewModel.materials.observe(viewLifecycleOwner) { materials ->
            showLoading(false)
            adapter = MaterialAdapter(materials) { material ->
                val intent = Intent(requireContext(), DetailMaterialActivity::class.java)
                intent.putExtra("MATERIAL_DATA", material)
                startActivity(intent)
            }
            binding.rvCourse.layoutManager = LinearLayoutManager(requireContext())
            binding.rvCourse.adapter = adapter
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.rvCourse) { view, insets ->
            val systemBarInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                view.paddingLeft,
                view.paddingTop,
                view.paddingRight,
                systemBarInsets.bottom
            )
            insets
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

