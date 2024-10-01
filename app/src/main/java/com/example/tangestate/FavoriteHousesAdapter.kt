package com.example.tangestate

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.w3c.dom.Text

private const val ACRES_TO_SQFT = 43560
class FavoriteHousesAdapter(private val context: Context, private val houses : MutableList<House>, private val sharedViewModel: SharedViewModel) : RecyclerView.Adapter<FavoriteHousesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.house_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val house = houses[position]
        holder.bind(house)
    }

    override fun getItemCount() = houses.size


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {

        private val housePrice = itemView.findViewById<TextView>(R.id.house_price_textview)
        private val houseBeds = itemView.findViewById<TextView>(R.id.house_beds_textview)
        private val houseBaths = itemView.findViewById<TextView>(R.id.house_baths_textview)
        private val houseSqft = itemView.findViewById<TextView>(R.id.house_sqft_textview)
        private val houseAddress = itemView.findViewById<TextView>(R.id.house_address_textview)
        private val houseStatus = itemView.findViewById<TextView>(R.id.house_status_textview)
        private val houseImage = itemView.findViewById<ImageView>(R.id.house_image)
        private val likeButton = itemView.findViewById<ImageView>(R.id.liked_houses_button)
        private val likeButtonOutline = itemView.findViewById<ImageView>(R.id.liked_houses_button_outline)
        private val cardView = itemView.findViewById<CardView>(R.id.house_card_view)

        fun bind(house: House) {
            val formattedPrice = "%,d".format(house.housePrice)
            housePrice.text = "$$formattedPrice"
            houseBaths.text = house.houseBaths.toString() + " ba | "
            houseBeds.text = house.houseBeds.toString() + " bds | "

            houseAddress.text = house.houseAddress
            houseStatus.text = house.houseStatus?.replace("_", " ")

            likeButtonOutline.setColorFilter(ContextCompat.getColor(context, R.color.lightRed))
            likeButton.setBackgroundColor(Color.TRANSPARENT)

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

            likeButtonOutline.setOnClickListener {
                likeButtonOutline.clearColorFilter()
                sharedViewModel.favoriteHouseItems[house] == false
                houses.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
                notifyItemRangeChanged(adapterPosition, houses.size)
                Log.d("House list size", houses.size.toString())
            }

            cardView.setOnClickListener {
                // Navigate to Details screen and pass selected house
                val intent = Intent(context, HouseDetailsActivity::class.java)
                intent.putExtra("HOUSE_EXTRA", house)
                context.startActivity(intent)
            }
        }
    }

}