package com.example.loomi.ui.article

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loomi.R
import com.example.loomi.data.retrofit.ApiConfig
import com.example.loomi.databinding.ActivityArticleBinding
import com.example.loomi.ui.adapter.ArticleAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActivityArticle : AppCompatActivity() {
    private lateinit var binding: ActivityArticleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val articles = withContext(Dispatchers.IO) {
                    ApiConfig.getApiService().getArticles()
                }

                val adapter = ArticleAdapter(articles) { article ->
                    val intent = Intent(this@ActivityArticle, DetailArticleActivity::class.java).apply {
                        putExtra("title", article.title)
                        putExtra("desc", article.description)
                        putExtra("image", article.coverImage?.toString() ?: article.socialImage)
                        putExtra("url", article.url)
                    }
                    startActivity(intent)
                }

                binding.rvArticle.apply {
                    layoutManager = LinearLayoutManager(this@ActivityArticle)
                    this.adapter = adapter
                }

            } catch (e: Exception) {
                Toast.makeText(this@ActivityArticle, "Gagal menampilkan artikel: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

    }
}