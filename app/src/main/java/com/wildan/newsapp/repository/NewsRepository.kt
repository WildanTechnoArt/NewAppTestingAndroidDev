package com.wildan.newsapp.repository

import com.wildan.newsapp.network.BaseApiService
import kotlinx.coroutines.flow.flow

class NewsRepository(private val baseApi: BaseApiService) {

    private val token = "724d78e162e04be89c67b917d80b8e26"

    fun getSourceList(category: String?) = flow {
        val response = baseApi.getSourceList("Bearer $token", category)
        emit(response)
    }

    fun getArticleList(page: Int?, sources: String?, search: String?) = flow {
        val response = baseApi.getArticleList("Bearer $token", sources, page, search)
        emit(response)
    }
}