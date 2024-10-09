package com.example.tangestate

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

private const val ACRES_TO_SQFT = 43560

class SearchHousesAdapter (private val context: Context,
                           private val houses : List<House>,
                           private val viewModel : SharedViewModel,
                           private val listener : OnItemClickListener) :
    RecyclerView.Adapter<SearchHousesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.house_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val house = houses[position]
        holder.bind(house)
    }

    fun updateItemColor(position : Int, likeStatus : Boolean) {
        val house = houses[position]
        if(viewModel.favoriteHouseItems.value?.get(house) == true) {
            viewModel.favoriteHouseItems.value?.set(house, false)
            if(viewModel.favoriteHouseItems.value!!.isNotEmpty()) {
                viewModel.favoriteHouseItems.value?.remove(house)
            }
        }
        else {
            viewModel.favoriteHouseItems.value?.set(house, true)
        }
        notifyItemChanged(position)
    }

    override fun getItemCount() = houses.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val housePrice = itemView.findViewById<TextView>(R.id.house_price_textview)
        private val houseBeds = itemView.findViewById<TextView>(R.id.house_beds_textview)
        private val houseBaths = itemView.findViewById<TextView>(R.id.house_baths_textview)
        private val houseSqft = itemView.findViewById<TextView>(R.id.house_sqft_textview)
        private val houseAddress = itemView.findViewById<TextView>(R.id.house_address_textview)
        private val houseStatus = itemView.findViewById<TextView>(R.id.house_status_textview)
        private val houseImage = itemView.findViewById<ImageView>(R.id.house_image)
        private val likeButtonOutline = itemView.findViewById<ImageView>(R.id.liked_houses_button_outline)
        private val cardView = itemView.findViewById<CardView>(R.id.house_card_view)


        fun bind(house: House) {
            if(house.housePrice == null) {
                housePrice.text = "Price: N/A"
            }
            else {
                val formattedPrice = "%,d".format(house.housePrice)
                housePrice.text = "$$formattedPrice"
            }
            if(house.houseBaths == null) {
                houseBaths.text = "n/a ba |"
            }
            else {
                houseBaths.text = house.houseBaths.toString() + " ba | "
            }
            if(house.houseBeds == null) {
                houseBeds.text = "n/a bds |"
            }
            else {
                houseBeds.text = house.houseBeds.toString() + " bds | "
            }
            houseAddress.text = house.houseAddress
            houseStatus.text = house.houseStatus?.replace("_", " ")

            if(house.houseAreaUnit == "sqft") {
                if(house.houseSqft == null) {
                    houseSqft.text = "n/a sqft"
                }
                else {
                    houseSqft.text = house.houseSqft.toInt().toString() + " sqft"
                }
            }
            else {
                if(house.houseSqft == null) {
                    houseSqft.text = "n/a sqft"
                }
                else {
                    houseSqft.text =
                        (house.houseSqft.times(ACRES_TO_SQFT)).toInt().toString() + " sqft"
                }
            }

            Glide.with(itemView)
                .load(house.houseImageUrl)
                .centerCrop()
                .into(houseImage)

            if(viewModel.favoriteHouseItems.value?.get(house) == true) {
                likeButtonOutline.setColorFilter(ContextCompat.getColor(context, R.color.lightRed))
            }
            else {
                likeButtonOutline.clearColorFilter()
            }

            likeButtonOutline.setOnClickListener {
                if(viewModel.favoriteHouseItems.value?.get(house) == true) {
                    likeButtonOutline.clearColorFilter()
                    viewModel.favoriteHouseItems.value?.set(house, false)
                    if(viewModel.favoriteHouseItems.value!!.isNotEmpty()) {
                        viewModel.favoriteHouseItems.value?.remove(house)
                    }
                }
                else {
                    likeButtonOutline.setColorFilter(ContextCompat.getColor(context, R.color.lightRed))
                    viewModel.favoriteHouseItems.value?.set(house, true)
                }
            }

            cardView.setOnClickListener {
                listener.onItemClick(house, viewModel.favoriteHouseItems.value?.get(house))
            }
        }
    }

}