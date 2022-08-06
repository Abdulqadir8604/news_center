package com.lamaq.newscenter

import android.content.ClipData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class Adapter(private val items: List<Model>) : RecyclerView.Adapter<Adapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.card_view_design, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemModel = items[position]
        holder.headline.text = itemModel.headline
        holder.description.text = itemModel.description
        Glide.with(holder.imageView.context)
            .load(itemModel.imageUrl)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val headline = itemView.findViewById<View>(R.id.headline_tv) as TextView
        val description = itemView.findViewById<View>(R.id.description_tv) as TextView
        val imageView = itemView.findViewById<View>(R.id.imageview) as ImageView
    }
}
