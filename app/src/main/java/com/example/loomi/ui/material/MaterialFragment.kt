package com.example.loomi.ui.material

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loomi.databinding.FragmentMaterialBinding
import com.example.loomi.ui.adapter.MaterialAdapter
import kotlin.jvm.java

class MaterialFragment : Fragment() {
    private var _binding: FragmentMaterialBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MaterialViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMaterialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[MaterialViewModel::class.java]
        binding.rvCourse.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCourse.isNestedScrollingEnabled = true
        binding.rvCourse.setHasFixedSize(true)
        viewModel.materials.observe(viewLifecycleOwner) { materials ->
            val adapter = MaterialAdapter(
                materials,
                onSectionClickCallback = { selectedSection ->
                    val bundle = Bundle().apply {
                        putParcelable("SECTION_DATA", selectedSection)
                    }
//                    findNavController().navigate(R.id.action_materialFragment_to_contentFragment, bundle)
                    Toast.makeText(context, "Fitur ini belum tersedia", Toast.LENGTH_SHORT).show()
                }
            )
            binding.rvCourse.adapter = adapter
        }
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
