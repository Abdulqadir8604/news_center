package com.lamaq.newscenter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.bumptech.glide.Glide


class Adapter(val context: Context, val articles: List<Articles>) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.card_view_design, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemModel = articles[position]
        holder.headline.text = itemModel.title
        holder.description.text = itemModel.description
        Glide.with(context)
            .load(itemModel.urlToImage)
            .into(holder.imageView)
        holder.readMore.setOnClickListener {
            val i: Intent = Intent(context, Webview::class.java)
            i.putExtra("url", itemModel.url)
            context.startActivity(i)
        }
    }

    override fun getItemCount(): Int = articles.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val headline = itemView.findViewById<View>(R.id.headline_tv) as TextView
        val description = itemView.findViewById<View>(R.id.description_tv) as TextView
        val imageView = itemView.findViewById<View>(R.id.imageview) as ImageView
        val readMore = itemView.findViewById<View>(R.id.readMore_tv) as TextView
        val source = itemView.findViewById<View>(R.id.source_tv) as TextView
    }
}
