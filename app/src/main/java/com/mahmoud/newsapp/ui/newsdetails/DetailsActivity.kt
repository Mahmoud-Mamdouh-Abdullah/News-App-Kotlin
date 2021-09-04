package com.mahmoud.newsapp.ui.newsdetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.mahmoud.newsapp.R
import com.mahmoud.newsapp.databinding.ActivityDetailsBinding
import com.mahmoud.newsapp.model.NewsModel
import com.squareup.picasso.Picasso

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailsBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_details)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val newsObjectString = intent.getStringExtra("newsObject")
        val gson = Gson()
        val newsModel = gson.fromJson(newsObjectString,NewsModel::class.java)

        binding.newsTitleTextview.text = newsModel.newsTitle
        val imgUrl: String = newsModel.newsImageURL
        if (imgUrl != "null" && imgUrl.isNotEmpty()) {
            Picasso.get().load(imgUrl).fit().into(binding.newsImageview)
        }
        val desc = newsModel.newsDescription
        if(desc == "null" || desc.isEmpty()) {
            binding.newsDescTextview.text = "الوصف غير متوفر"
        } else {
            binding.newsDescTextview.text = desc
        }
    }

    /**
     * Handling the Back Button Pressing in the action bar
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return true
    }
}