package com.example.tangestate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.bold
import androidx.recyclerview.widget.RecyclerView

class SearchHousesAdapter (private val houses : List<House>) : RecyclerView.Adapter<SearchHousesAdapter.ViewHolder>() {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // val foodName : TextView
        // val foodCalories : TextView

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each sub-view
        init {
            // foodName = itemView.findViewById(R.id.food_item_name)
            // foodCalories = itemView.findViewById(R.id.food_item_calories)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.house_item, parent, false)
        // Return a new holder instance
        return ViewHolder(contactView)
    }

    override fun getItemCount(): Int {
        return houses.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val house = houses.get(position)

//        holder.foodName.text = food.name
//        holder.foodCalories.text = food.calories.toString()

    }
}