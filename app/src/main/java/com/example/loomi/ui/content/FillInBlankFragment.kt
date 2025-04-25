package com.example.loomi.ui.content

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.loomi.R
import com.example.loomi.data.model.Content
import com.example.loomi.databinding.FragmentFillInBlankBinding
import com.example.loomi.ui.material.MaterialViewModel
import kotlin.collections.map


class FillInBlankFragment : Fragment() {

    private lateinit var binding: FragmentFillInBlankBinding
    private var inputs: MutableList<EditText> = mutableListOf()
    private var correctAnswer: List<String>? = null

    companion object {
        private const val ARG_CONTENT = "arg_content"

        fun newInstance(content: Content): FillInBlankFragment {
            val fragment = FillInBlankFragment()
            val bundle = Bundle().apply {
                putParcelable(ARG_CONTENT, content)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFillInBlankBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val content: Content? = arguments?.getParcelable(ARG_CONTENT)
        content?.let { setupQuiz(it) }
    }

    private fun setupQuiz(content: Content) {
        Log.d("FILL_IN_BLANK", "setupQuiz called with content: $content")

        val code = content.code ?: return
        val correctAnswers = content.correctAnswer
            ?.split(",")
            ?.map { it.trim().lowercase() }

        if (correctAnswers.isNullOrEmpty()) {
            Log.e("FILL_IN_BLANK", "Correct answer is missing!")
            return
        }

        binding.tvQuizTitle.text = content.title
        correctAnswer = correctAnswers

        val container = binding.quizDynamicContainer
        container.removeAllViews()
        inputs.clear()

        val parts = code.split("____")

        Log.d("FILL_IN_BLANK", "Parts setelah split: $parts")

        parts.forEachIndexed { index, part ->
            val tv = TextView(requireContext()).apply {
                text = part
                textSize = 16f
                setTextColor(Color.BLACK)
            }
            container.addView(tv)

            if (index < parts.lastIndex) {
                val input = EditText(requireContext()).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(0, 8, 0, 8)
                    }
                    inputType = InputType.TYPE_CLASS_TEXT
                    hint = "..."
                }

                input.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        val userAnswers = inputs.map { it.text.toString().trim().lowercase() }
                        Log.d("FILL_IN_BLANK", "User Answers: $userAnswers")

                        if (userAnswers.all { it.isNotEmpty() } && correctAnswer != null) {
                            if (userAnswers == correctAnswer) {
                                Toast.makeText(context, " Benar!", Toast.LENGTH_SHORT).show()
                                inputs.forEach { it.isEnabled = false }
                            } else {
                                Toast.makeText(context, "Coba lagi", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                })

                inputs.add(input)
                container.addView(input)
            }
        }
    }

}

