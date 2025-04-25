package com.example.loomi

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.loomi.data.model.Content
import com.example.loomi.data.model.ContentType
import com.example.loomi.databinding.ActivityContentBinding
import com.example.loomi.ui.content.ExplanationFragment
import com.example.loomi.ui.content.FillInBlankFragment
import com.example.loomi.ui.content.MultipleChoiceFragment
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
                val intent = Intent(this, FinishActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }
    private fun showCurrentContent() {
        val content = contents[currentIndex]
        binding.tvContentTitle.text = content.title

        val fragment = when (content.type) {
            ContentType.EXPLANATION -> ExplanationFragment.newInstance(content)
            ContentType.DRAG_AND_DROP -> DragAndDropFragment.newInstance(content)
            ContentType.MULTIPLE_CHOICE -> MultipleChoiceFragment.newInstance(content)
            ContentType.FILL_IN_THE_BLANK -> FillInBlankFragment.newInstance(content)

        }


        supportFragmentManager.beginTransaction()
            .replace(R.id.contentContainer, fragment)
            .commit()

        binding.btnNext.text = if (currentIndex == contents.size - 1) "Selesai" else "Materi selanjutnya"
    }
}