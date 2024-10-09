package com.example.tangestate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    var favoriteHouseItems : MutableLiveData<MutableMap<House?, Boolean>> = MutableLiveData(mutableMapOf())
}