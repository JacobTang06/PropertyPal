package com.example.tangestate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavoriteFragment : Fragment() {
    private lateinit var favoriteHousesAdapter : FavoriteHousesAdapter
    private lateinit var favoriteHousesRv : RecyclerView

    private lateinit var sharedViewModel : SharedViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        val likedHouses : MutableList<House> = mutableListOf()

        for(house in sharedViewModel.favoriteHouseItems.keys) {
            if(sharedViewModel.favoriteHouseItems[house] == true) {
                if (house != null) {
                    likedHouses.add(house)
                }
            }
        }

        val layoutManager = LinearLayoutManager(context)
        favoriteHousesRv = view.findViewById(R.id.favorite_houses_rv)
        favoriteHousesRv.layoutManager = layoutManager
        favoriteHousesRv.setHasFixedSize(true)
        favoriteHousesAdapter = FavoriteHousesAdapter(view.context, likedHouses, sharedViewModel)
        favoriteHousesRv.adapter = favoriteHousesAdapter

        return view
    }

    override fun onResume() {
        super.onResume()
        favoriteHousesAdapter.notifyDataSetChanged()
    }

    companion object {
        @JvmStatic
        fun newInstance() : FavoriteFragment {
            return FavoriteFragment()
        }
    }
}