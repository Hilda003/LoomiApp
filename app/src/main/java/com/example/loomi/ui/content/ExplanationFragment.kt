package com.example.loomi.ui.content

import android.R.attr.text
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.loomi.databinding.FragmentExplanationBinding
import com.example.loomi.data.model.Content


// Fragment to receive the Content data
class ExplanationFragment : Fragment() {

    private lateinit var content: Content

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
    ): View? {
        val binding = FragmentExplanationBinding.inflate(inflater, container, false)
        content = arguments?.getParcelable("content") ?: return binding.root

        // Display content data
//        binding.txtTitle.text = content.title
        binding.tvExplanation.text = content.description


        return binding.root
    }
}


