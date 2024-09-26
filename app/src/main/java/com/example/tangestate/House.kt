package com.example.tangestate

import android.support.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
//import com.google.gson.annotations.SerializedName

@Keep
@Serializable
data class BaseResponse (
    @SerialName("props")
    val properties : List<House>?
)

@Keep
@Serializable
data class House (
    @SerialName("imgSrc")
    val houseImageUrl : String?,
    @SerialName("price")
    val housePrice : Int?,
    @SerialName("bedrooms")
    val houseBeds : Int?,
    @SerialName("bathrooms")
    val houseBaths : Float?,
    @SerialName("lotAreaValue")
    val houseSqft : Float?,
    @SerialName("lotAreaUnit")
    val houseAreaUnit : String?,
    @SerialName("listingStatus")
    val houseStatus : String?,
    @SerialName("address")
    val houseAddress : String?,
    @SerialName("zpid")
    val houseId : Int?
) : java.io.Serializable

//    @SerialName("streetAddress")
//    var houseAddress : String?,
//    @SerialName("city")
//    var houseCity : String?,
//    @SerialName("zipcode")
//    var houseZipcode : Integer?,
//    @SerialName("state")
//    var houseState : String?,
//    @SerialName("atAGlanceFacts")
//    var facts : List<AtAGlanceFacts>?,
//    @SerialName("business_name")
//    var companyName : String?,
//    @SerialName("display_name")
//    var agentName : String?,
//    @SerialName("phone")
//    var agentNumber : String?,
//    @SerialName("description")
//    var houseDescription : String?

@Keep
@Serializable
data class AtAGlanceFacts(
    @SerialName("factLabel")
    var factLabel : String?,
    @SerialName("factValue")
    var factValue : String?,
) : java.io.Serializable


