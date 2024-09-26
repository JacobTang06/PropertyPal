package com.example.tangestate

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.w3c.dom.Text

class FavoriteHousesAdapter(private val context: Context, private val houses : MutableList<House>) : RecyclerView.Adapter<FavoriteHousesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.house_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val house = houses[position]
        holder.bind(house)
    }

    override fun getItemCount() = houses.size


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val housePrice = itemView.findViewById<TextView>(R.id.house_price_textview)
        private val houseBeds = itemView.findViewById<TextView>(R.id.house_beds_textview)
        private val houseBaths = itemView.findViewById<TextView>(R.id.house_baths_textview)
        private val houseSqft = itemView.findViewById<TextView>(R.id.house_sqft_textview)
        private val houseAddress = itemView.findViewById<TextView>(R.id.house_address_textview)
        private val houseStatus = itemView.findViewById<TextView>(R.id.house_status_textview)
        private val houseImage = itemView.findViewById<ImageView>(R.id.house_image)
        private val likeButton = itemView.findViewById<ImageButton>(R.id.liked_houses_button)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(house: House) {
            housePrice.text = house.housePrice.toString()
            houseBaths.text = house.houseBaths.toString()
            houseBeds.text = house.houseBeds.toString()
            houseSqft.text = house.houseSqft.toString()
            houseAddress.text = house.houseAddress
            houseStatus.text = house.houseStatus

            Glide.with(itemView)
                .load(house.houseImageUrl)
                .centerInside()
                .into(houseImage)

            likeButton.setOnClickListener {
                likeButton.setBackgroundResource(R.drawable.baseline_favorite_blank_24)
                // dislike house
                // need to pertain data and delete it from list
                houses.remove(house)
            }
        }

        override fun onClick(v : View?) {
            // Get selected house
            val house = houses[adapterPosition]

            // Navigate to Details screen and pass selected house
            val intent = Intent(context, HouseDetailsActivity::class.java)
            intent.putExtra("HOUSE_EXTRA", house)
            context.startActivity(intent)
        }

    }

}