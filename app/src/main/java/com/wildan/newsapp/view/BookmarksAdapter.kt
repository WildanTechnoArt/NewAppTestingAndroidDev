package com.wildan.newsapp.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wildan.newsapp.R
import com.wildan.newsapp.databinding.ItemArticlesBinding
import com.wildan.newsapp.model.Articles
import com.wildan.newsapp.utils.Constant

class BookmarksAdapter : RecyclerView.Adapter<BookmarksAdapter.Holder>() {

    private val dataList = mutableListOf<Articles>() // Dataset adapter

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newData: List<Articles>) {
        dataList.clear()
        dataList.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemArticlesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = dataList[position]
        val context = holder.itemView.context

        with(holder.binding) {
            val articleName = data.title ?: "-"
            Glide.with(context).load(data.urlToImage).into(imageArticles)
            textTitle.text = articleName
            textAuthor.text = "Author: ${data.author ?: "Unknown"}" +
                    " (${data.publishedAt?.let { Constant.formatPublishedDate(it) } ?: "No date"})"
            textDescription.text = data.description ?: "-"

            imageArticles.setOnClickListener {
                toDetailFragment(
                    data,
                    ImageViewActivity::class.java,
                    it.context,
                    data.url
                )
            }
            cardItem.setOnClickListener {
                toDetailFragment(
                    data,
                    ArticleViewActivity::class.java,
                    it.context,
                    data.url
                )
            }
            btnDetail.setOnClickListener {
                toDetailFragment(
                    data,
                    ArticleViewActivity::class.java,
                    it.context,
                    data.url
                )
            }
        }
    }

    private fun toDetailFragment(
        data: Articles,
        activity: Class<out AppCompatActivity>,
        context: Context,
        url: String?
    ) {
        if (url != null) {
            val bundle = Bundle().apply {
                putParcelable(Constant.CONTENT, data)
            }
            context.startActivity(Intent(context, activity).putExtras(bundle))
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.message_if_news_not_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun getItemCount(): Int = dataList.size

    class Holder(val binding: ItemArticlesBinding) : RecyclerView.ViewHolder(binding.root)
}
