package com.example.loomi.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loomi.R
import com.example.loomi.data.response.ResponseArticleItem
import com.example.loomi.databinding.ItemArticleBinding


class ArticleAdapter(
    private var articleList: List<ResponseArticleItem>,
    private val onClick: (ResponseArticleItem) -> Unit
) : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ResponseArticleItem) {
            binding.tvTitleArticle.text = article.title ?: "No Title"
            val imageUrl = article.coverImage?.toString() ?: article.socialImage

            if (imageUrl.isNotEmpty()) {
                Glide.with(binding.root.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.article1)
                    .into(binding.courseImg)
            } else {
                binding.courseImg.setImageResource(R.drawable.article1)
            }

            binding.root.setOnClickListener {
                onClick(article)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(articleList[position])
    }

    override fun getItemCount(): Int = articleList.size

    fun updateList(newList: List<ResponseArticleItem>) {
        articleList = newList
        notifyDataSetChanged()
    }
}


