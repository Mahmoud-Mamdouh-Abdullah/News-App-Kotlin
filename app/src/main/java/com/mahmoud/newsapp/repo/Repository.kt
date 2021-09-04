package com.mahmoud.newsapp.repo

import com.google.gson.JsonObject
import com.mahmoud.newsapp.api.NewsApi
import retrofit2.Call

class Repository constructor(private val newsApi: NewsApi){

    fun getNews(country: String, apiKey: String): Call<JsonObject> {
        return newsApi.getNews(country, apiKey)
    }
}