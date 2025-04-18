package com.example.loomi.ui.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.loomi.databinding.FragmentExplanationBinding
import com.example.loomi.model.Content

class ExplanationFragment : Fragment() {

    private var _binding: FragmentExplanationBinding? = null
    private val binding get() = _binding!!

    private var content: Content? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        content = arguments?.getParcelable("CONTENT_DATA")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExplanationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        content?.let {
            binding.tvTitle.text = it.title
            binding.tvDescription.text = it.description
        } ?: run {
            binding.tvTitle.text = "No content"
            binding.tvDescription.text = "Data tidak tersedia"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        // Pastikan newInstance ada
        fun newInstance(content: Content): ExplanationFragment {
            val fragment = ExplanationFragment()
            val args = Bundle()
            args.putParcelable("CONTENT_DATA", content)
            fragment.arguments = args
            return fragment
        }
    }
}