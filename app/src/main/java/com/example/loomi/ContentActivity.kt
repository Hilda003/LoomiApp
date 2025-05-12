package com.example.loomi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.loomi.data.model.Content
import com.example.loomi.data.model.ContentType
import com.example.loomi.databinding.ActivityContentBinding
import com.example.loomi.ui.content.DragAndDropFragment
import com.example.loomi.ui.content.ExplanationFragment
import com.example.loomi.ui.content.ExplanationFragment.OnExplanationPageCompleteListener
import com.example.loomi.ui.content.MultipleChoiceFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.jvm.java

class ContentActivity : AppCompatActivity(), ExplanationFragment.OnExplanationPageCompleteListener  {

    internal lateinit var binding: ActivityContentBinding
    private var contents: List<Content> = emptyList()
    private var currentIndex = 0
    private var isAnswerCorrect = false
    private var isLastExplanationPage = false

    private val TAG = "ContentActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        contents = (intent.getParcelableArrayListExtra<Content>("CONTENT_DATA") ?: emptyList())
            .sortedBy {
                when (it.type) {
                    ContentType.EXPLANATION -> 0
                    ContentType.DRAG_AND_DROP -> 1
                    ContentType.MULTIPLE_CHOICE -> 2
                }
            }
        showCurrentContent()

        binding.btnNext.setOnClickListener {
            val currentContent = contents[currentIndex]
            if (currentContent.type == ContentType.EXPLANATION) {
                if (isLastExplanationPage) {
                    moveToNextPage()
                } else {
                    val current = supportFragmentManager.findFragmentById(R.id.contentContainer)
                    if (current is ExplanationFragment) {
                        current.goToNextExplanationPage()
                    }
                }
            } else {
                if (isAnswerCorrect) moveToNextPage()
            }
        }

        binding.icBack.setOnClickListener {
            if (currentIndex > 0) {
                currentIndex--
                showCurrentContent()
            } else {
                showExitConfirmationDialog()
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (currentIndex > 0) {
                    currentIndex--
                    showCurrentContent()
                } else {
                    showExitConfirmationDialog()
                }
            }
        })
    }



    private fun showCurrentContent() {
        val content = contents[currentIndex]
        binding.tvContentTitle.text = content.title

        val fragment = when (content.type) {
            ContentType.EXPLANATION -> ExplanationFragment.newInstance(content)
            ContentType.DRAG_AND_DROP -> DragAndDropFragment.newInstance(content)
            ContentType.MULTIPLE_CHOICE -> MultipleChoiceFragment.newInstance(content)
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.contentContainer, fragment)
            .commit()

        isAnswerCorrect = false
        when (content.type) {
            ContentType.EXPLANATION -> {
                setButtonState(isEnabled = false, text = "Tap untuk melanjutkan")
            }
            else -> {
                setButtonState(isEnabled = false, text = "Cek Hasil")
            }
        }
        val progress = ((currentIndex + 1).toFloat() / contents.size * 100).toInt()
        binding.progressBar.progress = progress
        binding.icBack.visibility = if (currentIndex == 0) View.GONE else View.VISIBLE
    }

    internal fun moveToNextPage() {
        if (currentIndex < contents.size - 1) {
            currentIndex++
            showCurrentContent()
        } else {
            val sectionId = intent.getStringExtra("SECTION_ID") ?: ""
            val materialId = intent.getStringExtra("MATERIAL_ID") ?: ""
            val intent = Intent(this, FinishActivity::class.java)
            intent.putExtra("SECTION_ID", sectionId)
            intent.putExtra("MATERIAL_ID", materialId)
            startActivity(intent)
            finish()
        }
    }

    private fun showExitConfirmationDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Konfirmasi")
            .setMessage("Apakah kamu yakin ingin keluar dari materi ini?")
            .setPositiveButton("Ya") { _, _ -> finish() }
            .setNegativeButton("Tidak", null)
            .show()
    }

    fun setButtonState(isEnabled: Boolean, text: String, showButton: Boolean = true) {
        binding.btnNext.apply {
            visibility = if (showButton) View.VISIBLE else View.GONE
            this.isEnabled = isEnabled
            this.text = text

            val backgroundColor = if (isEnabled) {
                ContextCompat.getColor(context, R.color.active_button)
            } else {
                ContextCompat.getColor(context, R.color.disabled_button)
            }
            setBackgroundColor(backgroundColor)
        }
    }

    fun isLastContent(): Boolean {
        return currentIndex == contents.size - 1
    }


    fun setAnswerCorrect(correct: Boolean) {
        isAnswerCorrect = correct
    }

    override fun onExplanationPageComplete(isLastPage: Boolean) {
        isLastExplanationPage = isLastPage
        setButtonState(
            isEnabled = true,
            text = if (isLastPage) if (isLastContent()) "Selesai" else "Lanjut" else "Lanjut",
            showButton = true
        )
    }
}