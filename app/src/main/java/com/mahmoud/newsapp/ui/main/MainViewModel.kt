package com.mahmoud.newsapp.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.mahmoud.newsapp.repo.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel (private val repo: Repository) : ViewModel() {
    val newsJson = MutableLiveData<JsonObject>()
    val errorMsg = MutableLiveData<String>()

    fun getNews (country: String, apiKey: String) {
        val response = repo.getNews(country, apiKey)
        response.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                newsJson.postValue(response.body())
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                errorMsg.postValue(t.message)
            }

        })
    }
}