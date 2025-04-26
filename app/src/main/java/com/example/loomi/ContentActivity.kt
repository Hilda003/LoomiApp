package com.example.loomi

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.loomi.data.model.Content
import com.example.loomi.data.model.ContentType
import com.example.loomi.databinding.ActivityContentBinding
import com.example.loomi.ui.content.ExplanationFragment
import com.example.loomi.ui.content.FillInBlankFragment
import com.example.loomi.ui.content.MultipleChoiceFragment
import com.example.loomi.utils.ProgressManager
import kotlin.jvm.java

class ContentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContentBinding
    private var contents: List<Content> = emptyList()
    private var currentIndex = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityContentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        contents = intent.getParcelableArrayListExtra("CONTENT_DATA") ?: emptyList()

        showCurrentContent()

        binding.btnNext.setOnClickListener {
            if (currentIndex < contents.size - 1) {
                currentIndex++
                showCurrentContent()
            } else {
                val sectionId = intent.getIntExtra("SECTION_ID", 0)
                val materialId = intent.getStringExtra("MATERIAL_ID") ?: ""
                ProgressManager.markSectionCompleted(this, sectionId)

                val intent = Intent(this, FinishActivity::class.java)
                intent.putExtra("SECTION_ID", sectionId)
                intent.putExtra("MATERIAL_ID", materialId)
                startActivity(intent)
                finish()
            }
        }

    }
    private fun showCurrentContent() {
        val content = contents[currentIndex]
        binding.tvContentTitle.text = content.title

        binding.btnNext.visibility = View.GONE

        val fragment = when (content.type) {
            ContentType.EXPLANATION -> ExplanationFragment.newInstance(content)
            ContentType.DRAG_AND_DROP -> DragAndDropFragment.newInstance(content)
            ContentType.MULTIPLE_CHOICE -> MultipleChoiceFragment.newInstance(content)
            ContentType.FILL_IN_BLANK -> FillInBlankFragment.newInstance(content)
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.contentContainer, fragment)
            .commit()

        binding.btnNext.text = if (currentIndex == contents.size - 1) "Selesai" else "Lanjut"
        val progress = ((currentIndex + 1).toFloat() / contents.size * 100).toInt()
        binding.progressBar.progress = progress
    }
    fun showBtnNext() {
        binding.btnNext.visibility = View.VISIBLE
    }
    fun hideBtnNext() {
        binding.btnNext.visibility = View.GONE
    }
}