package com.example.loomi.ui.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.loomi.data.model.Content
import com.example.loomi.databinding.FragmentMultipleChoiceBinding

class MultipleChoiceFragment : Fragment() {

    private var _binding: FragmentMultipleChoiceBinding? = null
    private val binding get() = _binding!!

    private lateinit var content: Content

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = arguments?.getParcelable("content") ?: Content()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMultipleChoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvCode.text = content.code ?: ""
        binding.tvQuestion.text = content.question ?: ""
        binding.tvQuestion.textSize = 16f

        val choices = content.choices ?: listOf()
        val correctAnswer = content.correctAnswer

        choices.forEach { choice ->
            val radioButton = RadioButton(requireContext()).apply {
                text = choice
                id = View.generateViewId()
                textSize = 14f
            }
            binding.rgChoices.addView(radioButton)
        }

        binding.rgChoices.setOnCheckedChangeListener { group, checkedId ->
            val selectedRadioButton = group.findViewById<RadioButton>(checkedId)
            val selectedAnswer = selectedRadioButton?.text.toString()

            val message = if (selectedAnswer == correctAnswer) {
                "Jawaban kamu benar 🎉"
            } else {
                "Jawaban kamu salah 😢\nJawaban yang benar: $correctAnswer"

            }

            AlertDialog.Builder(requireContext())
                .setTitle("Hasil Jawaban")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(content: Content): MultipleChoiceFragment {
            val fragment = MultipleChoiceFragment()
            fragment.arguments = Bundle().apply {
                putParcelable("content", content)
            }
            return fragment
        }
    }
}
