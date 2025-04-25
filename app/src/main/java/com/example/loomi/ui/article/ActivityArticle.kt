package com.example.loomi.ui.article

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loomi.R
import com.example.loomi.data.response.ResponseArticleItem
import com.example.loomi.data.retrofit.ApiConfig
import com.example.loomi.databinding.ActivityArticleBinding
import com.example.loomi.ui.adapter.ArticleAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.filter

class ActivityArticle : AppCompatActivity() {

    private lateinit var binding: ActivityArticleBinding
    private lateinit var adapter: ArticleAdapter
    private val viewModel: ArticleViewModel by viewModels()
    private var selectedCategory: Button? = null
    private var allArticles: List<ResponseArticleItem> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupInsets()
        setupSearchBar()

        adapter = ArticleAdapter(emptyList()) { article ->
            val intent = Intent(this, DetailArticleActivity::class.java).apply {
                putExtra("title", article.title)
                putExtra("desc", article.description)
                putExtra("image", article.coverImage?.toString() ?: article.socialImage)
                putExtra("url", article.url)
            }
            startActivity(intent)
        }

        binding.rvArticle.layoutManager = LinearLayoutManager(this)
        binding.rvArticle.adapter = adapter

        observeViewModel()
        viewModel.fetchArticles()
    }

    private fun setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupSearchBar() {
        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                val filtered = allArticles.filter {
                    it.title.contains(query, ignoreCase = true) ||
                            it.description.contains(query, ignoreCase = true)
                }
                adapter.updateList(filtered)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun observeViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.articles.collectLatest { articles ->
                allArticles = articles
                adapter.updateList(articles)
//                setupCategoryButtons()
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.error.collectLatest { error ->
                error?.let {
                    Toast.makeText(this@ActivityArticle, "Error: $it", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

//    private fun setupCategoryButtons() {
//        val buttons = listOf(binding.btnTech, binding.btnDesign, binding.btnCareer)
//
//        buttons.forEach { button ->
//            button.setOnClickListener {
//                selectedCategory?.isSelected = false
//                selectedCategory?.setTextColor(ContextCompat.getColor(this, R.color.dark_green))
//
//                button.isSelected = true
//                button.setTextColor(ContextCompat.getColor(this, android.R.color.white))
//                selectedCategory = button
//
//                val category = button.text.toString()
//                val filtered = allArticles.filter { it.tags?.contains(category, ignoreCase = true) == true }
//                adapter.updateList(filtered)
//            }
//        }
//    }
}
