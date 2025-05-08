package com.example.loomi.ui.content


import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.loomi.ContentActivity
import com.example.loomi.R
import com.example.loomi.data.model.ChunkType
import com.example.loomi.databinding.FragmentExplanationBinding
import com.example.loomi.data.model.Content
import com.example.loomi.data.model.ExplanationChunk
import com.example.loomi.data.model.parseChunks

class ExplanationFragment : Fragment() {

    private lateinit var content: Content
    private lateinit var explanationChunks: List<ExplanationChunk>
    private var currentPageIndex = 0
    private var currentChunkIndexInPage = 0
    private val chunksPerPage = 4

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
        content = arguments?.getParcelable("content") ?: Content()
        explanationChunks = parseChunks(content.descriptionList)

        showNextChunkInPage()

        binding.tvTapToContinue.setOnClickListener {
            val totalChunksShown = currentPageIndex * chunksPerPage + currentChunkIndexInPage + 1
            if (totalChunksShown < explanationChunks.size) {
                if (currentChunkIndexInPage < chunksPerPage - 1) {
                    currentChunkIndexInPage++
                } else {
                    currentPageIndex++
                    currentChunkIndexInPage = 0
                    binding.llExplanationContainer.removeAllViews()
                }
                showNextChunkInPage()
            }
        }

        return binding.root
    }

    private fun showNextChunkInPage() {
        val chunkIndex = currentPageIndex * chunksPerPage + currentChunkIndexInPage
        if (chunkIndex < explanationChunks.size) {
            val chunk = explanationChunks[chunkIndex]
            val context = requireContext()

            val textView = TextView(context).apply {
                text = chunk.content.trim()
                setLineSpacing(8f, 1.2f)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = 16
                }

                when (chunk.type) {
                    ChunkType.TEXT -> {
                        setTextAppearance(R.style.PoppinsRegular)
                    }
                    ChunkType.CODE -> {
                        setTypeface(Typeface.MONOSPACE)
                        setBackgroundColor(ContextCompat.getColor(context, R.color.light_gray))
                        setTextColor(Color.DKGRAY)
                        setPadding(16, 16, 16, 16)
                        text = chunk.content.formatAsCode()
                    }
                    ChunkType.OUTPUT -> {
                        setTypeface(Typeface.DEFAULT_BOLD)
                        background = ContextCompat.getDrawable(context, R.drawable.bg_output)
                        setTextColor(Color.DKGRAY)
                        setPadding(16, 12, 16, 12)
                    }
                }
            }

            binding.llExplanationContainer.addView(textView)

            val isLastChunk = chunkIndex == explanationChunks.size - 1
            if (!isLastChunk) {
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

    fun String.formatAsCode(): String {
        return this
            .replace("\\n", "\n")
            .replace("\\t", "\t")
            .replace("\\\"", "\"")
            .replace("\\'", "'")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
