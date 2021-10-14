package com.example.assisment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.assisment.Item
import com.example.assisment.R
import com.example.assisment.adapter.TrendingAdapter.TrendHolder
import com.example.assisment.room.Entity
import com.squareup.picasso.Picasso

class TrendingAdapter(var list: List<Entity>) : RecyclerView.Adapter<TrendHolder>() {

    fun update(list: List<Entity>){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_design, parent, false)
        return TrendHolder(view)
    }

    override fun onBindViewHolder(holder: TrendHolder, position: Int) {
        val title: TextView = holder.title
        val thumbnail:ImageView=holder.thumbnail

        val item=list[position]
        title.text=item.title
        Picasso.get()
            .load(item.thumbnailUrl)
            .noFade()
            .into(thumbnail)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class TrendHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val thumbnail = itemView.findViewById<ImageView>(R.id.thumbnail)!!
        val title = itemView.findViewById<TextView>(R.id.title)!!
    }
}