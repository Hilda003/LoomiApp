package com.example.loomi

import android.content.ClipData
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.loomi.data.model.Content
import com.example.loomi.databinding.FragmentDragAndDropBinding
import com.example.loomi.BottomSheetResult

class DragAndDropFragment : Fragment() {

    private var _binding: FragmentDragAndDropBinding? = null
    private val binding get() = _binding!!

    private val answers = mutableListOf<String?>()
    private var correctAnswers: List<String> = emptyList()
    private var hasAnsweredCorrectly = false

    companion object {
        private const val ARG_CONTENT = "arg_content"

        fun newInstance(content: Content): DragAndDropFragment {
            return DragAndDropFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_CONTENT, content)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDragAndDropBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val content = arguments?.getParcelable<Content>(ARG_CONTENT)
        if (content == null) {
            return
        }

        binding.tvQuestionText.text = content.description ?: ""
        binding.tvQuestionText.setTextColor(Color.BLACK)

        correctAnswers = content.correctAnswer.orEmpty()

        answers.clear()
        repeat(correctAnswers.size) { i ->
            answers.add(null)
            val slot = createPlaceholderSlot(i)
            binding.answerContainer.addView(slot)
        }

        val optionViews = mutableListOf<View>()
        content.choices.orEmpty().forEach { item ->
            val option = createDraggableOption(item).apply { id = View.generateViewId() }
            binding.optionsRoot.addView(option)
            optionViews.add(option)
        }

        binding.optionsFlow.referencedIds = optionViews.map { it.id }.toIntArray()
        (activity as? ContentActivity)?.setButtonState(false, "Cek Hasil", true)

        (activity as? ContentActivity)?.binding?.btnNext?.setOnClickListener {
            if (hasAnsweredCorrectly) {
                (activity as? ContentActivity)?.moveToNextPage()
            } else {
                checkAnswer()
            }
        }
    }

    private fun createPlaceholderSlot(index: Int): TextView {
        return TextView(requireContext()).apply {
            text = "___"
            setBackgroundResource(R.drawable.bg_placeholder)
            setPadding(24, 16, 24, 16)
            textSize = 16f
            setTextColor(Color.BLACK)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { marginEnd = 16 }
            setOnDragListener { v, event ->
                if (event.action == DragEvent.ACTION_DROP) {
                    val dragged = event.localState as? TextView ?: return@setOnDragListener true
                    val target = v as? TextView ?: return@setOnDragListener true

                    target.text = dragged.text
                    target.setBackgroundResource(R.drawable.bg_filled_slot)
                    target.setTextColor(Color.BLACK)

                    dragged.visibility = View.GONE

                    answers[index] = dragged.text.toString()

                    if (answers.all { it != null }) {
                        (activity as? ContentActivity)?.setButtonState(true, "Cek Hasil", true)
                    }
                }
                true
            }
        }
    }

    private fun createDraggableOption(text: String): TextView {
        return TextView(requireContext()).apply {
            this.text = text
            setBackgroundResource(R.drawable.bg_options)
            setPadding(24, 16, 24, 16)
            textSize = 16f
            setTextColor(Color.BLACK)
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )

            setOnLongClickListener {
                val shadow = View.DragShadowBuilder(this)
                val clipData = ClipData.newPlainText("label", text)
                startDragAndDrop(clipData, shadow, this, 0)
                true
            }
        }
    }

    private fun checkAnswer() {
        if (answers.contains(null)) return

        val isCorrect = answers.filterNotNull() == correctAnswers

        if (isCorrect) {
            hasAnsweredCorrectly = true
            (activity as? ContentActivity)?.setAnswerCorrect(true)

            val bottomSheet = BottomSheetResult.newInstance(true) {
                (activity as? ContentActivity)?.moveToNextPage()
            }
            bottomSheet.show(parentFragmentManager, "BottomSheetResult")

        } else {
            val bottomSheet = BottomSheetResult.newInstance(false)
            bottomSheet.show(parentFragmentManager, "BottomSheetResult")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
