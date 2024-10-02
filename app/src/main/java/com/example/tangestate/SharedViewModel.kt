package com.example.tangestate

import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    var favoriteHouseItems : MutableMap<House?, Boolean> = mutableMapOf()
}