package com.wildan.newsapp.view

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.wildan.newsapp.R
import com.wildan.newsapp.databinding.ActivityArticleViewBinding
import com.wildan.newsapp.model.Articles
import com.wildan.newsapp.utils.Constant
import com.wildan.newsapp.utils.ViewBindingExt.viewBinding
import com.wildan.newsapp.viewmodel.DatabaseViewModel
import com.wildan.newsapp.viewmodel.LocalDataViewModelFactory

class ArticleViewActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityArticleViewBinding::inflate)
    private var getArticle: Articles? = null
    private lateinit var viewModel: DatabaseViewModel
    private var bookmarkMenuItem: MenuItem? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val factory = LocalDataViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[DatabaseViewModel::class.java]

        getArticle = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Constant.CONTENT, Articles::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(Constant.CONTENT)
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = getArticle?.title ?: "Unknown"
        }

        getArticle?.url?.let { url ->
            viewModel.checkIfBookmarked(url)
        }

        binding.webView.settings.javaScriptEnabled = true
        binding.swipeRefresh.setOnRefreshListener {
            loadWebView()
        }

        loadWebView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        bookmarkMenuItem = menu.findItem(R.id.action_bookmark)
        viewModel.isBookmarked.observe(this) { isBookmarked ->
            bookmarkMenuItem?.setIcon(
                if (isBookmarked) R.drawable.ic_bookmark_filled
                else R.drawable.ic_bookmark_outline
            )
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_bookmark -> {
                getArticle?.let { article ->
                    val bookmark = Articles(
                        url = article.url,
                        title = article.title,
                        author = article.author,
                        description = article.description,
                        urlToImage = article.urlToImage,
                        publishedAt = article.publishedAt,
                        content = article.content
                    )

                    viewModel.toggleBookmark(bookmark) { isSaved ->
                        val message = if (isSaved) "Add Bookmark" else "Remove Bookmark"
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }
                true
            }

            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadWebView() = with(binding) {
        swipeRefresh.isRefreshing = true
        getArticle?.url?.let { url ->
            webView.loadUrl(url)
        }

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                request?.url?.toString()?.let { webView.loadUrl(it) }
                swipeRefresh.isRefreshing = true
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                swipeRefresh.isRefreshing = true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                swipeRefresh.isRefreshing = false
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && binding.webView.canGoBack()) {
            binding.webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}