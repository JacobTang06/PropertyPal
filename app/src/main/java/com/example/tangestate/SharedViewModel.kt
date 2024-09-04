package com.example.tangestate

import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    var favoriteHouseItems : MutableList<House> = mutableListOf()
}