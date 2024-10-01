package com.example.tangestate

import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    var favoriteHouseItems : MutableMap<House?, Boolean> = mutableMapOf()
    // var zpidToHouses : MutableList<House> = mutableListOf()
}