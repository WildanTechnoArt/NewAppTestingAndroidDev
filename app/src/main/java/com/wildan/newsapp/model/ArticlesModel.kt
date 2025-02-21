package com.wildan.newsapp.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ArticlesModel(
    @SerializedName("status") var status: String? = null,
    @SerializedName("totalResults") var totalResults: Long? = null,
    @SerializedName("articles") var articles: ArrayList<Articles> = arrayListOf()
)

@Parcelize
@Entity(
    tableName = "TableArticles"
)
data class Articles(
    @PrimaryKey
    @ColumnInfo("url") var url: String,
    @ColumnInfo("id") var id: Int = 0,
    @ColumnInfo("author") var author: String? = null,
    @ColumnInfo("title") var title: String? = null,
    @ColumnInfo("description") var description: String? = null,
    @ColumnInfo("urlToImage") var urlToImage: String? = null,
    @ColumnInfo("publishedAt") var publishedAt: String? = null,
    @ColumnInfo("content") var content: String? = null
) : Parcelable