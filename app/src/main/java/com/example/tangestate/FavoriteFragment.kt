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

    //private val favoriteHousesItems : MutableList<House> = mutableListOf()
    private lateinit var sharedViewModel : SharedViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        val layoutManager = LinearLayoutManager(context)
        favoriteHousesRv = view.findViewById(R.id.favorite_houses_rv)
        favoriteHousesRv.layoutManager = layoutManager
        favoriteHousesRv.setHasFixedSize(true)
        favoriteHousesAdapter = FavoriteHousesAdapter(view.context, sharedViewModel.favoriteHouseItems)
        favoriteHousesRv.adapter = favoriteHousesAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance() : FavoriteFragment {
            return FavoriteFragment()
        }
    }
}