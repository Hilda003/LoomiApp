package com.example.loomi

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.loomi.data.model.Content
import com.example.loomi.databinding.ActivityContentBinding
import kotlin.jvm.java

class ContentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContentBinding
    private var contents: List<Content> = emptyList()
    private var currentIndex = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityContentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBarsInsets.left + 35,
                systemBarsInsets.top + 35,
                systemBarsInsets.right + 35,
                systemBarsInsets.bottom + 35
            )
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
        binding.tvContentType.text = content.type.name
        binding.tvContentTitle.text = content.title
        binding.tvContentDescription.text = content.description
        if (currentIndex == contents.size - 1) {
            binding.btnNext.text = "Selesai"
        } else {
            binding.btnNext.text = "Materi selanjutnya"
        }
    }
}