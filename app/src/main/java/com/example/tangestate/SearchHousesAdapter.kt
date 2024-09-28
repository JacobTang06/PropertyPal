package com.example.tangestate

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.DecimalFormat

private const val ACRES_TO_SQFT = 43560

class SearchHousesAdapter (private val context: Context, private val houses : List<House>, private val viewModel : SharedViewModel) :
    RecyclerView.Adapter<SearchHousesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.house_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val house = houses[position]
        holder.bind(house)
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
        private val likeButton = itemView.findViewById<ImageButton>(R.id.liked_houses_button)
        private val cardView = itemView.findViewById<CardView>(R.id.house_card_view)


        fun bind(house: House) {
            val formattedPrice = "%,d".format(house.housePrice)
            housePrice.text = "$$formattedPrice"
            houseBaths.text = house.houseBaths.toString() + " ba | "
            houseBeds.text = house.houseBeds.toString() + " bds | "

            houseAddress.text = house.houseAddress
            houseStatus.text = house.houseStatus?.replace("_", " ")

            if(house.houseAreaUnit == "sqft") {
                houseSqft.text = house.houseSqft?.toInt().toString() + " sqft"
            }
            else {
                houseSqft.text = (house.houseSqft?.times(ACRES_TO_SQFT))?.toInt().toString() + " sqft"
            }

            Glide.with(itemView)
                .load(house.houseImageUrl)
                .centerCrop()
                .into(houseImage)

            likeButton.setOnClickListener {
                if(viewModel.likeStatus) {
                    likeButton.background.clearColorFilter()
                    viewModel.likeStatus = false
                    if(viewModel.favoriteHouseItems.isNotEmpty()) {
                        viewModel.favoriteHouseItems.remove(house)
                    }
                }
                else {
                    likeButton.setBackgroundResource(R.color.lightRed)
                    viewModel.likeStatus = true
                    viewModel.favoriteHouseItems.add(house)
                }
            }

            cardView.setOnClickListener {
                val intent = Intent(context, HouseDetailsActivity::class.java)
                intent.putExtra("HOUSE_EXTRA", house)
                context.startActivity(intent)
            }
        }

    }

}