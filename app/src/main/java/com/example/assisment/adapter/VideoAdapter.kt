package com.example.assisment.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.assisment.R
import com.example.assisment.adapter.VideoAdapter.TrendHolder
import com.example.assisment.room.Entity
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class VideoAdapter(var list: List<Entity>, private val context: Context) :
    RecyclerView.Adapter<TrendHolder>() {
    /**
     * @param list this is the list of new entities + previous
     * @updates the list of recycler view with new items added to it
     */
    fun update(list: List<Entity>) {
        val startPosition = this.list.size + 1
        val numberOfItems = list.size - this.list.size
        this.list = list
        notifyItemRangeInserted(startPosition, numberOfItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_design, parent, false)
        return TrendHolder(view)
    }

    override fun onBindViewHolder(holder: TrendHolder, position: Int) {
        val title: TextView = holder.title
        val thumbnail: ImageView = holder.thumbnail
        val onClick: LinearLayout = holder.onClick
        val imageProgress: ProgressBar = holder.imageProgress
        val item = list[position]

        title.text = item.title
        Picasso.get()
            .load(item.thumbnailUrl)
            .noFade()
            .into(thumbnail, object : Callback {
                override fun onSuccess() {
                    imageProgress.visibility = GONE
                }

                override fun onError(e: Exception?) {
                    thumbnail.setImageResource(R.drawable.not_found)
                    imageProgress.visibility = GONE
                }
            })
        /**
         * @Starts video on Youtube app
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
        val imageProgress = itemView.findViewById<ProgressBar>(R.id.image_progress)!!
    }
}