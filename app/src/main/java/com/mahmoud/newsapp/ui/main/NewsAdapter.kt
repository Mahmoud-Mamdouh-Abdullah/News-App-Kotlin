package com.mahmoud.newsapp.ui.main

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mahmoud.newsapp.R
import com.mahmoud.newsapp.model.NewsModel
import com.squareup.picasso.Picasso

class NewsAdapter(private val mOnItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private var newsList: List<NewsModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.newsTitle.text = newsList[position].newsTitle
        val img: String = newsList[position].newsImageURL
        if (img != "null" && img.isNotEmpty()) {
            Picasso.get().load(newsList[position].newsImageURL).fit().into(holder.newsImage)
        } else {
            holder.newsImage.setImageResource(R.drawable.default_news_image_front)
        }
        holder.newsSourceName.text = newsList[position].newsSourceName

        holder.itemView.setOnClickListener {
            mOnItemClickListener.onItemClick(newsList[position])
        }
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    fun setList(newsList: List<NewsModel>) {
        this.newsList = newsList
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(n: NewsModel)
    }

    class NewsViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val newsTitle: TextView = itemView.findViewById(R.id.news_title_textview)
        val newsImage: ImageView = itemView.findViewById(R.id.news_imageview)
        val newsSourceName: TextView = itemView.findViewById(R.id.news_source_textview)
    }
}