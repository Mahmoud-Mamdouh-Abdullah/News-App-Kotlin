package com.mahmoud.newsapp.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.mahmoud.newsapp.R
import com.mahmoud.newsapp.api.NewsApi
import com.mahmoud.newsapp.api.NewsRestAPI
import com.mahmoud.newsapp.api.NewsRestAPI.Companion.API_Key
import com.mahmoud.newsapp.databinding.ActivityMainBinding
import com.mahmoud.newsapp.model.NewsModel
import com.mahmoud.newsapp.repo.Repository
import com.mahmoud.newsapp.ui.newsdetails.DetailsActivity
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), NewsAdapter.OnItemClickListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var newsAdapter : NewsAdapter
    private var newsList : List<NewsModel> = ArrayList()
    private lateinit var newsApi: NewsApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )

        init()
        binding.newsRecyclerview.adapter = newsAdapter


        //observing on the news mutable live data which hold the GET request Data
        viewModel.newsJson.observe(this, Observer {
            newsList = parseJson(it.toString())
            newsAdapter.setList(newsList)
            binding.indicator.visibility = View.INVISIBLE
        })

        //observing on the error mutable live data which hold the GET request error message
        viewModel.errorMsg.observe(this, Observer {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        })

        viewModel.getNews("eg", API_Key)
    }

    /**
     * variables initialization
     */
    private fun init() {
        newsApi = NewsRestAPI.getInstance()
        viewModel = ViewModelProvider(
            this,
            MyViewModelFactory(Repository(newsApi))
        ).get(MainViewModel::class.java)
        newsAdapter = NewsAdapter(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        val item = menu?.findItem(R.id.search_btn)
        val searchView = item?.actionView as SearchView

        /**
         * listening to the text change in the search view to search in the news
         */
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchText = newText!!.lowercase()
                val tempList : ArrayList<NewsModel> = ArrayList()
                newsList.forEach {
                    if(it.newsTitle.contains(searchText) || it.newsSourceName.lowercase().contains(searchText)) {
                        tempList.add(it)
                    }
                }
                newsAdapter.setList(tempList)
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * this methode for parsing the json request
     * @param newsJson the json string of the GET request
     * @return newsList
     */
    private fun parseJson(newsJson : String) : List<NewsModel> {
        val newsList: ArrayList<NewsModel> =  ArrayList()
        val rootObj = JSONObject(newsJson)
        val newsArr = rootObj.getJSONArray("articles")
        for(i in 0 until newsArr.length()-1) {
            val article : JSONObject = newsArr.getJSONObject(i)
            val title = article.getString("title")
            val description = article.getString("description")
            val img = article.getString("urlToImage")
            val sourceName = article.getJSONObject("source").getString("name")

            val newsModel = NewsModel(img, title, sourceName, description)
            newsList.add(newsModel)
        }
        return newsList
    }

    /**
     * Handling the news item click in the recycler view
     */
    override fun onItemClick(n: NewsModel) {
        val gson = Gson()
        val intent = Intent(this, DetailsActivity::class.java).apply {
            putExtra("newsObject", gson.toJson(n))
        }
        startActivity(intent)
    }
}