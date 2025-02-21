package com.wildan.newsapp.view

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.wildan.newsapp.databinding.ActivityImageViewBinding
import com.wildan.newsapp.model.Articles
import com.wildan.newsapp.utils.Constant
import com.wildan.newsapp.utils.ViewBindingExt.viewBinding

class ImageViewActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityImageViewBinding::inflate)
    private var getUrlImage: String? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val getUrlImage: String? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Constant.CONTENT, Articles::class.java)?.urlToImage
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<Articles>(Constant.CONTENT)?.urlToImage
        }
        Glide.with(this)
            .load(getUrlImage)
            .into(binding.imageView)
    }
}