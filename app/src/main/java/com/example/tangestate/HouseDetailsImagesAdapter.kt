package com.example.tangestate

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class HouseDetailsImagesAdapter(private val context: Context, private val images : List<String>) : RecyclerView.Adapter<HouseDetailsImagesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.house_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = images[position]
        holder.bind(image)
    }

    override fun getItemCount() = images.size


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        private val houseImage : ImageView = itemView.findViewById(R.id.houseImage)

        fun bind(image: String) {
            Glide.with(context)
                .load(image)
                .centerCrop()
                .into(houseImage)
        }
    }

}