package com.example.loomi

import android.content.ClipData
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.loomi.data.model.Content
import com.example.loomi.databinding.FragmentDragAndDropBinding



class DragAndDropFragment : Fragment() {

    private var _binding: FragmentDragAndDropBinding? = null
    private val binding get() = _binding!!

    private val answers = mutableListOf<String?>()

    companion object {
        private const val ARG_CONTENT = "arg_content"

        fun newInstance(content: Content): DragAndDropFragment {
            val fragment = DragAndDropFragment()
            val args = Bundle()
            args.putParcelable(ARG_CONTENT, content)
            fragment.arguments = args
            return fragment
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
        Log.d("DragAndDropFragment", "Received content: $content")

        if (content == null) {
            Log.e("DragAndDropFragment", "Content is null!")
            return
        }

        val questionText = content.description ?: ""
        val choices = content.choices.orEmpty()
        val correctAnswers = content.correctAnswer?.split(",")?.map { it.trim() }.orEmpty()

        binding.tvQuestionText.text = questionText
        answers.clear()
        val placeholders = mutableListOf<TextView>()
        repeat(correctAnswers.size) { i ->
            answers.add(null)
            val slot = createPlaceholderSlot(i)
            binding.answerContainer.addView(slot)
            placeholders.add(slot)
        }

        // Create option views
        val optionViews = mutableListOf<View>()
        choices.forEach { item ->
            val option = createDraggableOption(item).apply {
                id = View.generateViewId()
            }
            binding.optionsRoot.addView(option)
            optionViews.add(option)
        }

        binding.optionsFlow.referencedIds = optionViews.map { it.id }.toIntArray()
    }

    private fun createPlaceholderSlot(index: Int): TextView {
        return TextView(requireContext()).apply {
            text = "___"
            setBackgroundResource(R.drawable.bg_placeholder)
            setPadding(24, 16, 24, 16)
            textSize = 16f
            setTextColor(android.graphics.Color.BLACK)
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
                    dragged.visibility = View.GONE

                    answers[index] = dragged.text.toString()

                    if (answers.all { it != null }) {
                        validateAnswers()
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
            setTextColor(android.graphics.Color.WHITE)
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

    private fun validateAnswers() {
        val correctAnswers = arguments?.getParcelable<Content>(ARG_CONTENT)
            ?.correctAnswer?.split(",")?.map { it.trim() } ?: return

        val isCorrect = correctAnswers == answers.map { it ?: "" }

        val message = if (isCorrect) "Yeay, jawaban kamu benar! 🎉" else "Ups, masih salah 😢"

        AlertDialog.Builder(requireContext())
            .setTitle("Hasil")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
