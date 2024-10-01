package com.example.tangestate

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestHeaders
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException

private const val TAG = "HouseDetailsActivity"
private const val ACRES_TO_SQFT = 43560
private const val API_KEY = BuildConfig.API_KEY

class HouseDetailsActivity : AppCompatActivity() {
    private lateinit var housePrice : TextView
    private lateinit var houseBeds : TextView
    private lateinit var houseBaths : TextView
    private lateinit var houseSqft : TextView
    private lateinit var houseAddress : TextView
    private lateinit var houseStatus : TextView
    private lateinit var houseImage : ImageView
    private lateinit var likeButton : ImageView
    private lateinit var likeButtonOutline : ImageView

    private lateinit var listingAgent : TextView
    private lateinit var listingCompany : TextView
    private lateinit var listingAgentNumber : TextView
    private lateinit var houseSummary : TextView
    private lateinit var houseFacts : TextView

    private lateinit var houseImage1 : ImageView
    private lateinit var houseImage2 : ImageView
    private lateinit var houseImage3 : ImageView
    private lateinit var houseImage4 : ImageView

    private lateinit var clickedHouse : House
    private val client = AsyncHttpClient()
    private var params = RequestParams()
    private var headers = RequestHeaders()

    private lateinit var sharedViewModel : SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_house_details)

        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)

        setupHouseDetails()
        fetchHouseDetails()
        fetchHouseImages()

    }

    private fun setupHouseDetails() {
        housePrice = findViewById(R.id.house_details_price_textview)
        houseBeds = findViewById(R.id.house_details_bed_textview)
        houseBaths = findViewById(R.id.house_details_bath_textview)
        houseSqft = findViewById(R.id.house_details_sqft_textview)
        houseAddress = findViewById(R.id.house_details_address_textview)
        houseStatus = findViewById(R.id.house_details_status_textview)
        houseImage = findViewById(R.id.house_details_image)
        likeButton = findViewById(R.id.house_details_liked_button)
        likeButtonOutline = findViewById(R.id.house_details_liked_button_outline)

        listingAgent = findViewById(R.id.house_listing_agent_textview)
        listingCompany = findViewById(R.id.house_listing_company_textview)
        listingAgentNumber = findViewById(R.id.house_listing_agent_number_textview)
        houseSummary = findViewById(R.id.house_summary_textview)
        houseFacts = findViewById(R.id.house_facts_textview)

        houseImage1 = findViewById(R.id.houseImage1)
        houseImage2 = findViewById(R.id.houseImage2)
        houseImage3 = findViewById(R.id.houseImage3)
        houseImage4 = findViewById(R.id.houseImage4)

    }

    private fun fetchHouseDetails() {
        headers["x-rapidapi-key"] = API_KEY
        headers["x-rapidapi-host"] = "zillow-com1.p.rapidapi.com"

        clickedHouse = intent.getSerializableExtra("HOUSE_EXTRA") as House

        params["zpid"] = clickedHouse.houseId.toString()

        var clientUrl = "https://zillow-com1.p.rapidapi.com/property"

        client.get(clientUrl, headers, params, object: JsonHttpResponseHandler()
        {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "onFailure $response")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.i(TAG, "onSuccess: JSON properties data $json")

                try {
                    val house = createJson().decodeFromString(
                        HouseDetails.serializer(),
                        json.jsonObject.toString()
                    )

                    displayHouseDetails(house)

                } catch (e: JSONException) {
                    Log.e(TAG, "Exception: $e")
                }
            }
        })
    }

    private fun fetchHouseImages() {
        headers["x-rapidapi-key"] = API_KEY
        headers["x-rapidapi-host"] = "zillow-com1.p.rapidapi.com"

        clickedHouse = intent.getSerializableExtra("HOUSE_EXTRA") as House

        params["zpid"] = clickedHouse.houseId.toString()

        var clientUrl = "https://zillow-com1.p.rapidapi.com/images"

        client.get(clientUrl, headers, params, object: JsonHttpResponseHandler()
        {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "onFailure $response")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.i(TAG, "onSuccess: JSON properties data $json")

                try {
                    val house = createJson().decodeFromString(
                        HouseImages.serializer(),
                        json.jsonObject.toString()
                    )

                    val imageView = listOf(houseImage1, houseImage2, houseImage3, houseImage4)
                    for(imageIndex in imageView.indices) {
                        Glide.with(this@HouseDetailsActivity)
                            .load(house.houseImages[imageIndex + (house.houseImages.size / imageView.size)])
                            .centerCrop()
                            .into(imageView[imageIndex])
                    }

                } catch (e: JSONException) {
                    Log.e(TAG, "Exception: $e")
                }
            }
        })
    }

    private fun displayHouseDetails(house : HouseDetails) {
        val formattedPrice = "%,d".format(house.price)
        housePrice.text = "$$formattedPrice"
        houseBaths.text = house.baths.toString() + " ba | "
        houseBeds.text = house.beds.toString() + " bds | "

        houseAddress.text = house.address + ", " + house.city + ", " + house.state + " " + house.zipcode
        houseStatus.text = house.homeStatus?.replace("_", " ")

        if(house.sqftUnits == "sqft") {
            houseSqft.text = house.sqft?.toInt().toString() + " sqft"
        }
        else {
            houseSqft.text = (house.sqft?.times(ACRES_TO_SQFT))?.toInt().toString() + " sqft"
        }

        Glide.with(this)
            .load(house.imageUrl)
            .centerCrop()
            .into(houseImage)

        // Null check for agent information
        val agentNumber = "(" + house.houseListingInfo?.agentNumber?.prefix + ")" +
                house.houseListingInfo?.agentNumber?.areaCode + "-" +
                house.houseListingInfo?.agentNumber?.number

        listingAgent.text = "Agent: " + house.houseListingInfo?.agentName
        listingCompany.text = "Company: " + house.houseListingInfo?.companyName
        listingAgentNumber.text = "Number: $agentNumber"
        houseSummary.text = "Description: " + house.homeDescription

        for(fact in house.houseFacts?.facts!!) {
            houseFacts.text = houseFacts.text.toString() + fact.factLabel + ": " + fact.factValue + "\n"
        }

        likeButton.setOnClickListener {
            if(sharedViewModel.favoriteHouseItems[clickedHouse] == true) {
                likeButton.clearColorFilter()
                sharedViewModel.favoriteHouseItems[clickedHouse] = false
                if(sharedViewModel.favoriteHouseItems.isNotEmpty()) {
                    sharedViewModel.favoriteHouseItems.remove(clickedHouse)
                }
            }
            else {
                likeButton.setBackgroundResource(R.color.lightRed)
                sharedViewModel.favoriteHouseItems[clickedHouse] = true
            }
        }

    }
}