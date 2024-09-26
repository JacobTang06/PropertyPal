package com.example.tangestate

import android.os.Bundle
import android.util.Log
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
    private lateinit var likeButton : ImageButton

    private lateinit var listingAgent : TextView
    private lateinit var listingCompany : TextView
    private lateinit var houseSummary : TextView
    private lateinit var houseFacts : TextView

    private lateinit var sharedViewModel : SharedViewModel

    private lateinit var house : House

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_house_details)

        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)

        setupHouseDetails()
        house = intent.getSerializableExtra("HOUSE_EXTRA") as House
        fetchHouseDetails()
        displayHouseDetails()

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

        listingAgent = findViewById(R.id.house_listing_agent_textview)
        listingCompany = findViewById(R.id.house_listing_company_textview)
        houseSummary = findViewById(R.id.house_summary_textview)
        houseFacts = findViewById(R.id.house_facts_textview)

    }

    private fun fetchHouseDetails() {
        val client = AsyncHttpClient()
        var params = RequestParams()
        var headers = RequestHeaders()
        //params["X-RapidAPI-Key"] = API_KEY
        headers["x-rapidapi-key"] = API_KEY
        headers["x-rapidapi-host"] = "zillow-com1.p.rapidapi.com"

        params["zpid"] = house.houseId.toString()

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
                    val parsedJsonHouse = createJson().decodeFromString(
                        House.serializer(),
                        json.jsonObject.toString()
                    )
                    house = parsedJsonHouse

                } catch (e: JSONException) {
                    Log.e(TAG, "Exception: $e")
                }
            }
        })
    }

    private fun displayHouseDetails() {

        housePrice.text = "$" + house.housePrice.toString()
        houseBaths.text = house.houseBaths.toString() + " ba | "
        houseBeds.text = house.houseBeds.toString() + " bds | "

        houseAddress.text = house.houseAddress
        houseStatus.text = house.houseStatus?.replace("_", " ")

        if(house.houseAreaUnit == "sqft") {
            houseSqft.text = house.houseSqft?.toInt().toString() + " sqft"
        }
        else {
            houseSqft.text = (house.houseSqft?.times(ACRES_TO_SQFT))?.toInt().toString() + " sqft"
        }

        Glide.with(this)
            .load(house.houseImageUrl)
            .centerCrop()
            .into(houseImage)
//        listingAgent.text = house.agentName
//        listingCompany.text = house.companyName
//        houseSummary.text = house.houseDescription

//        for(fact in house.facts!!) {
//            houseFacts.text = fact.factLabel + ": " + fact.factValue + "\n"
//        }

        Glide.with(this@HouseDetailsActivity)
            .load(house.houseImageUrl)
            .centerInside()
            .into(houseImage)

        likeButton.setOnClickListener {
            if(sharedViewModel.likeStatus) {
                likeButton.background.clearColorFilter()
                sharedViewModel.likeStatus = false
                if(sharedViewModel.favoriteHouseItems.isNotEmpty()) {
                    sharedViewModel.favoriteHouseItems.remove(house)
                }
            }
            else {
                likeButton.setBackgroundResource(R.color.lightRed)
                sharedViewModel.likeStatus = true
                sharedViewModel.favoriteHouseItems.add(house)
            }
        }

    }
}