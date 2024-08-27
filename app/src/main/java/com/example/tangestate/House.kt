package com.example.tangestate

import com.google.gson.annotations.SerializedName

class House {
    @SerializedName("image_url")
    var houseImageUrl : String? = null

    @SerializedName("price")
    var housePrice : Float? = null

    @SerializedName("bedrooms")
    var houseBeds : Integer? = null

    @SerializedName("bathrooms")
    var houseBath : Float? = null

    @SerializedName("livingArea")
    var houseSqft : Float? = null

    @SerializedName("homeStatus")
    var houseStatus : String? = null

    @SerializedName("homeType")
    var houseType : String? = null

    @SerializedName("streetAddress")
    var houseAddress : String? = null

    @SerializedName("city")
    var houseCity : String? = null

    @SerializedName("zipcode")
    var houseZipcode : Integer? = null

    @SerializedName("state")
    var houseState : String? = null
}