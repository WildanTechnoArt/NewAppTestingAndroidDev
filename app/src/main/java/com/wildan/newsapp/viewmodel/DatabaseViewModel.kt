package com.wildan.newsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wildan.newsapp.database.DatabaseRepository
import com.wildan.newsapp.model.Articles
import kotlinx.coroutines.launch

class DatabaseViewModel(private val repository: DatabaseRepository) : ViewModel() {

    val allData: LiveData<List<Articles>> = repository.getListBookmark()

    val isBookmarked: LiveData<Boolean> = repository.isBookmarked

    fun checkIfBookmarked(url: String) {
        repository.checkIfBookmarked(url)
    }

    fun toggleBookmark(data: Articles, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            if (isBookmarked.value == true) {
                repository.removeBookmark(data)
                callback(false)
            } else {
                repository.addBookmark(data)
                callback(true)
            }
        }
    }
}