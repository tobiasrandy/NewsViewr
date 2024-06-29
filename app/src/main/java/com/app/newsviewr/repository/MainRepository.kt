package com.app.newsviewr.repository

import com.app.newsviewr.network.ApiService
import javax.inject.Inject

class MainRepository @Inject constructor (private val apiService: ApiService) {

    suspend fun getNewsList() = apiService.getNewsList()

}