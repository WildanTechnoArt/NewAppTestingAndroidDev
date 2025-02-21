package com.wildan.newsapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.wildan.newsapp.model.Articles

@Dao
interface DataDao {

    @Query("SELECT * FROM TableArticles ORDER BY id ASC")
    fun getAllBookmark(): LiveData<List<Articles>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBookmark(orders: Articles): Long

    @Delete
    suspend fun removeBookmark(data: Articles)

    @Query("SELECT * FROM TableArticles WHERE url = :url LIMIT 1")
    fun isArticleBookmarked(url: String): LiveData<Articles?>
}