package com.example.assisment.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.assisment.App
import com.example.assisment.R
import com.example.assisment.adapter.TrendingAdapter.TrendHolder
import com.example.assisment.room.Entity
import com.squareup.picasso.Picasso

class TrendingAdapter(var list: List<Entity>,val context: Context) : RecyclerView.Adapter<TrendHolder>() {
    /*
    updates the list of recycler view with updated items
     */
    fun update(list: List<Entity>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_design, parent, false)
        return TrendHolder(view)
    }

    override fun onBindViewHolder(holder: TrendHolder, position: Int) {
        val title: TextView = holder.title
        val thumbnail: ImageView = holder.thumbnail
        val onClick: LinearLayout = holder.onClick
        val item = list[position]
        title.text = item.title

        Picasso.get()
            .load(item.thumbnailUrl)
            .noFade()
            .into(thumbnail)
        /*
        Starts video
         */
        onClick.setOnClickListener {
            val videoLink = item.videoLink
            val uri = Uri.parse("https://www.youtube.com/watch?v=$videoLink")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setPackage("com.google.android.youtube")
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class TrendHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val thumbnail = itemView.findViewById<ImageView>(R.id.thumbnail)!!
        val title = itemView.findViewById<TextView>(R.id.title)!!
        val onClick = itemView.findViewById<LinearLayout>(R.id.list_item)!!
    }
}