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
    // For property extended search
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

@Keep
@Serializable
data class HouseDetails (
    // For property search
    @SerialName("imgSrc")
    val imageUrl : String?,
    @SerialName("price")
    val price : Int?,
    @SerialName("bedrooms")
    val beds : Int?,
    @SerialName("bathrooms")
    val baths : Float?,
    @SerialName("livingAreaValue")
    val sqft : Float?,
    @SerialName("livingAreaUnits")
    val sqftUnits : String?,
    @SerialName("homeStatus")
    val homeStatus : String?,
    @SerialName("streetAddress")
    val address : String?,
    @SerialName("city")
    val city : String?,
    @SerialName("state")
    val state : String?,
    @SerialName("zipcode")
    val zipcode : String?,
    @SerialName("description")
    var homeDescription : String?,
    @SerialName("resoFacts")
    var houseFacts : HouseFacts?,
    @SerialName("listed_by")
    var houseListingInfo : HouseListing?
) : java.io.Serializable

@Keep
@Serializable
data class HouseFacts (
    @SerialName("atAGlanceFacts")
    var facts : List<AtAGlanceFacts>?
)

@Keep
@Serializable
data class HouseListing (
    @SerialName("business_name")
    var companyName : String?,
    @SerialName("display_name")
    var agentName : String?,
    @SerialName("phone")
    var agentNumber : Number?
)

@Keep
@Serializable
data class Number (
    // Access JSON data properly
    @SerialName("prefix")
    var prefix : String?,
    @SerialName("areacode")
    var areaCode : String?,
    @SerialName("number")
    var number : String?,
)

@Keep
@Serializable
data class AtAGlanceFacts(
    @SerialName("factLabel")
    var factLabel : String?,
    @SerialName("factValue")
    var factValue : String?,
) : java.io.Serializable


