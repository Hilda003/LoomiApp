package com.example.loomi.ui.content


import android.os.Bundle
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
            val fragment = ExplanationFragment()
            val bundle = Bundle()
            bundle.putParcelable("content", content)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExplanationBinding.inflate(inflater, container, false)
        content = arguments?.getParcelable("content") ?: return binding.root

        textChunks = content.descriptionList
        showCurrentChunk()

        val tapListener = View.OnClickListener {
            if (currentChunkIndex < textChunks.size - 1) {
                currentChunkIndex++
                showCurrentChunk()
            }
        }

        binding.tvTapToContinue.setOnClickListener(tapListener)

        return binding.root
    }

    private fun showCurrentChunk() {
        binding.tvExplanation.text = textChunks[currentChunkIndex]

        if (currentChunkIndex < textChunks.size - 1) {
            binding.tvTapToContinue.visibility = View.VISIBLE
            (activity as? ContentActivity)?.hideBtnNext()
        } else {
            binding.tvTapToContinue.visibility = View.GONE
            (activity as? ContentActivity)?.showBtnNext()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



