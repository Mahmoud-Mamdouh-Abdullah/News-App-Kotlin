package com.mahmoud.newsapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsRestAPI {

    companion object {
        var newsApi: NewsApi? = null

        fun getInstance(): NewsApi {
            if (newsApi == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                newsApi = retrofit.create(NewsApi::class.java)
            }
            return newsApi!!
        }

        private const val BASE_URL: String = "https://newsapi.org/v2/"
        const val API_Key: String = "63b1f94dad044add871d1e319c630265"
    }

}