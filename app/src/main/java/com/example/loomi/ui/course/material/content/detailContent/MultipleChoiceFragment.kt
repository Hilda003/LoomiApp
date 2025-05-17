package com.example.loomi.ui.course.material.content.detailContent

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import com.example.loomi.ui.course.material.content.ContentActivity
import com.example.loomi.data.model.Content
import com.example.loomi.databinding.FragmentMultipleChoiceBinding
import com.example.loomi.utils.BottomSheetResult
import com.example.loomi.R
import com.example.loomi.utils.unescapeJava

class MultipleChoiceFragment : Fragment() {

    private var _binding: FragmentMultipleChoiceBinding? = null
    private val binding get() = _binding!!

    private lateinit var content: Content
    private var selectedAnswer: String? = null
    private var correctAnswer: String? = null
    private var hasAnsweredCorrectly = false

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

        binding.tvCode.text = content.code?.unescapeJava() ?: ""
        binding.tvQuestion.text = content.question ?: ""
        binding.tvQuestion.textSize = 16f
        binding.tvQuestion.setTextColor(Color.BLACK)

        val choices = content.choices ?: emptyList()
        correctAnswer = content.correctAnswer?.firstOrNull()

        choices.forEach { choice ->
            val radioButton = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_radio_choice, binding.rgChoices, false) as RadioButton
            radioButton.text = choice
            radioButton.id = View.generateViewId()
            binding.rgChoices.addView(radioButton)
        }


        (activity as? ContentActivity)?.setButtonState(false, "Cek Hasil")

        binding.rgChoices.setOnCheckedChangeListener { group, checkedId ->
            val selectedRadioButton = group.findViewById<RadioButton>(checkedId)
            selectedAnswer = selectedRadioButton?.text.toString()
            (activity as? ContentActivity)?.playSoundIfEnabled(R.raw.click)
            (activity as? ContentActivity)?.setButtonState(true, "Cek Hasil")
        }

        (activity as? ContentActivity)?.binding?.btnNext?.setOnClickListener {
            if (selectedAnswer.isNullOrEmpty()) return@setOnClickListener

            if (hasAnsweredCorrectly) {
                (activity as? ContentActivity)?.moveToNextPage()
            } else {
                checkAnswer()
            }
        }
    }

    private fun checkAnswer() {
        val isCorrect = selectedAnswer == correctAnswer

        if (isCorrect) {
            hasAnsweredCorrectly = true
            (activity as? ContentActivity)?.setAnswerCorrect(true)

            val bottomSheet = BottomSheetResult.newInstance(true) {
                (activity as? ContentActivity)?.moveToNextPage()
            }
            bottomSheet.show(parentFragmentManager, "BottomSheetResult")

        } else {
            (activity as? ContentActivity)?.setAnswerCorrect(false)
            val bottomSheet = BottomSheetResult.newInstance(false)
            bottomSheet.show(parentFragmentManager, "BottomSheetResult")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(content: Content): MultipleChoiceFragment {
            return MultipleChoiceFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("content", content)
                }
            }
        }
    }
}
