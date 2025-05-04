package com.example.loomi.ui.content

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.loomi.ContentActivity
import com.example.loomi.databinding.FragmentExplanationBinding
import com.example.loomi.data.model.Content

class ExplanationFragment : Fragment() {

    private lateinit var content: Content
    private lateinit var textChunks: List<String>
    private var currentChunkIndex = 0

    private var _binding: FragmentExplanationBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(content: Content): ExplanationFragment {
            return ExplanationFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("content", content)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExplanationBinding.inflate(inflater, container, false)
        val receivedContent = arguments?.getParcelable<Content>("content")
        if (receivedContent == null) {
            textChunks = content.descriptionList.toList()
            showCurrentChunk()
            return binding.root
        }

        content = receivedContent
//        textChunks = content.descriptionList.takeIf { it.isNotEmpty() }
//            ?: listOf("Tidak ada penjelasan tersedia.")
        showCurrentChunk()

        binding.tvTapToContinue.setOnClickListener {
            if (currentChunkIndex < textChunks.size - 1) {
                currentChunkIndex++
                showCurrentChunk()
            }
        }
        return binding.root
    }

    private fun showCurrentChunk() {
        if (currentChunkIndex < textChunks.size) {
            if (currentChunkIndex == 0) {
                binding.tvExplanation.text = textChunks[currentChunkIndex]
            } else {
                binding.tvExplanation.append("\n\n${textChunks[currentChunkIndex]}")
            }

            if (currentChunkIndex < textChunks.size - 1) {
                binding.tvTapToContinue.visibility = View.VISIBLE
                (activity as? ContentActivity)?.setButtonState(
                    isEnabled = false,
                    text = "Tap untuk melanjutkan",
                    showButton = false
                )
            } else {
                binding.tvTapToContinue.visibility = View.GONE
                (activity as? ContentActivity)?.setButtonState(
                    isEnabled = true,
                    text = if ((activity as? ContentActivity)?.isLastContent() == true) "Selesai" else "Lanjut",
                    showButton = true
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
