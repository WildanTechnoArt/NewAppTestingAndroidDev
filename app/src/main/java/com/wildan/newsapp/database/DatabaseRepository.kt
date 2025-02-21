package com.wildan.newsapp.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.wildan.newsapp.model.Articles

class DatabaseRepository private constructor(private val dataDao: DataDao) {

    private val _isBookmarked = MediatorLiveData<Boolean>()
    val isBookmarked: LiveData<Boolean> get() = _isBookmarked

    private var lastSource: LiveData<Articles?>? = null

    companion object {
        @Volatile
        private var instance: DatabaseRepository? = null

        fun getInstance(context: Context): DatabaseRepository {
            return instance ?: synchronized(this) {
                instance ?: DatabaseRepository(AppDatabase.getInstance(context).dataDao())
                    .also { instance = it }
            }
        }
    }

    fun getListBookmark(): LiveData<List<Articles>> = dataDao.getAllBookmark()

    suspend fun addBookmark(data: Articles) = dataDao.addBookmark(data)

    suspend fun removeBookmark(data: Articles) = dataDao.removeBookmark(data)

    fun checkIfBookmarked(url: String) {
        lastSource?.let { _isBookmarked.removeSource(it) }

        val source = dataDao.isArticleBookmarked(url)
        lastSource = source

        _isBookmarked.addSource(source) { bookmarked ->
            _isBookmarked.value = bookmarked != null
        }
    }
}