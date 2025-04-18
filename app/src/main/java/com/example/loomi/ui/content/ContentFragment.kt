package com.example.loomi.ui.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.loomi.databinding.FragmentContentBinding
import com.example.loomi.model.Content
import com.example.loomi.model.ContentType
import com.example.loomi.model.Section


class ContentFragment : Fragment() {

    private var contents: List<Content> = emptyList()
    private var currentIndex = 0
    private var _binding: FragmentContentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val section = arguments?.getParcelable<Section>("SECTION_DATA")
        if (section == null) {
            binding.tvTitleContent.text = "No section data"
            return
        }

        contents = section.content
        binding.tvTitleContent.text = section.title

        showContent(currentIndex)

        binding.btnNext.setOnClickListener {
            if (currentIndex < contents.size - 1) {
                currentIndex++
                showContent(currentIndex)
            } else {
                // TODO: Handle selesai materi
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun showContent(index: Int) {
        val content = contents[index]

        val fragment = when (content.type) {
            ContentType.EXPLANATION -> ExplanationFragment.newInstance(content)
            else -> {

                throw IllegalArgumentException("Unknown content type: ${content.type}")
            }
        }

        childFragmentManager.beginTransaction()
            .replace(binding.navHostFragment.id, fragment)
            .commit()

        updateProgress()
    }

    private fun updateProgress() {
        val progress = ((currentIndex + 1).toFloat() / contents.size) * 100
        binding.progressBar.progress = progress.toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
