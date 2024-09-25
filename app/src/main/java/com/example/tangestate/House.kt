package com.example.tangestate

import android.support.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
//import com.google.gson.annotations.SerializedName


@Keep
@Serializable
data class BaseResponse(
    @SerialName("properties")
    val properties: List<House>?
)
@Keep
@Serializable
data class House (
    @SerialName("image_url")
    var houseImageUrl : String?,
    @SerialName("price")
    var housePrice : Float?,
    @SerialName("bedrooms")
    var houseBeds : Int?,
    @SerialName("bathrooms")
    var houseBaths : Float?,
    @SerialName("livingArea")
    var houseSqft : Float?,
    @SerialName("homeStatus")
    var houseStatus : String?,
    @SerialName("streetAddress")
    var houseAddress : String?,
    @SerialName("city")
    var houseCity : String?,
    @SerialName("zipcode")
    var houseZipcode : Integer?,
    @SerialName("state")
    var houseState : String?,
    @SerialName("atAGlanceFacts")
    var facts : List<AtAGlanceFacts>?,
//    @SerialName("hasHeating")
//    var heating : Boolean?,
//    @SerialName("hasCooling")
//    var cooling : Boolean?,
//    @SerialName("parking")
//    var parkingSpots : Integer?,
    @SerialName("business_name")
    var companyName : String?,
    @SerialName("display_name")
    var agentName : String?,
    @SerialName("description")
    var houseDescription : String?
) : java.io.Serializable

@Keep
@Serializable
data class AtAGlanceFacts(
    @SerialName("factLabel")
    var factLabel : String?,
    @SerialName("factValue")
    var factValue : String?,
) : java.io.Serializable


