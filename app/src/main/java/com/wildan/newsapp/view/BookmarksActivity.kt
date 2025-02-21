package com.wildan.newsapp.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.wildan.newsapp.databinding.ActivityBookmarksBinding
import com.wildan.newsapp.utils.ViewBindingExt.viewBinding
import com.wildan.newsapp.viewmodel.DatabaseViewModel
import com.wildan.newsapp.viewmodel.LocalDataViewModelFactory
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class BookmarksActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityBookmarksBinding::inflate)
    private lateinit var viewModelDatabase: DatabaseViewModel
    private var mAdapter by Delegates.notNull<BookmarksAdapter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        mAdapter = BookmarksAdapter()

        val factory = LocalDataViewModelFactory.getInstance(this)
        viewModelDatabase =
            ViewModelProvider(this, factory)[DatabaseViewModel::class.java]

        binding.rvArticles.apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(this@BookmarksActivity)
            adapter = mAdapter
        }

        viewModelDatabase.allData.observe(this) { pagingData ->
            lifecycleScope.launch {
                mAdapter.submitList(pagingData)

                val itemCount = pagingData.size
                if (itemCount > 0) {
                    binding.rvArticles.visibility = View.VISIBLE
                    binding.textMessage.visibility = View.GONE
                } else {
                    binding.rvArticles.visibility = View.GONE
                    binding.textMessage.visibility = View.VISIBLE
                }
            }
        }
    }
}