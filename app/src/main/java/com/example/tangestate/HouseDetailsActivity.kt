package com.example.tangestate

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private lateinit var houseImagesRv : RecyclerView
    private var houseImages : MutableList<String> = mutableListOf()

    private lateinit var clickedHouse : House

    private val client = AsyncHttpClient()
    private var params = RequestParams()
    private var headers = RequestHeaders()

    private lateinit var sharedViewModel : SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_house_details)

        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]

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

        houseImagesRv = findViewById(R.id.house_images_rv)

    }

    private fun fetchHouseDetails() {
        headers["x-rapidapi-key"] = API_KEY
        headers["x-rapidapi-host"] = "zillow-com1.p.rapidapi.com"

        clickedHouse = intent.getSerializableExtra("HOUSE_EXTRA") as House

        params["zpid"] = clickedHouse.houseId.toString()

        val clientUrl = "https://zillow-com1.p.rapidapi.com/property"

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

        val clientUrl = "https://zillow-com1.p.rapidapi.com/images"

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

                    houseImages.addAll(house.houseImages)

                    val layoutManager = LinearLayoutManager(this@HouseDetailsActivity, LinearLayoutManager.HORIZONTAL, false)
                    houseImagesRv.layoutManager = layoutManager
                    houseImagesRv.setHasFixedSize(true)
                    val houseImagesAdapter = HouseDetailsImagesAdapter(this@HouseDetailsActivity, houseImages)
                    houseImagesRv.adapter = houseImagesAdapter

                } catch (e: JSONException) {
                    Log.e(TAG, "Exception: $e")
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
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

        val likeStatus = intent.getBooleanExtra("LIKE_STATUS", false)

        if(likeStatus) {
            likeButtonOutline.setColorFilter(ContextCompat.getColor(this, R.color.lightRed))
            sharedViewModel.favoriteHouseItems.value?.set(clickedHouse, true)
        }
        else {
            likeButtonOutline.clearColorFilter()
            sharedViewModel.favoriteHouseItems.value?.set(clickedHouse, false)
        }

        likeButtonOutline.setOnClickListener {
            if(sharedViewModel.favoriteHouseItems.value?.get(clickedHouse) == true) {
                likeButtonOutline.clearColorFilter()
                sharedViewModel.favoriteHouseItems.value?.set(clickedHouse, false)
                if(sharedViewModel.favoriteHouseItems.value!!.isNotEmpty()) {
                    sharedViewModel.favoriteHouseItems.value?.remove(clickedHouse)              }
            }
            else {
                likeButtonOutline.setColorFilter(ContextCompat.getColor(this, R.color.lightRed))
                sharedViewModel.favoriteHouseItems.value?.set(clickedHouse, true)
            }
        }

    }

    override fun onBackPressed() {
        Log.d("Back button", "pressed")
        val position = intent.getIntExtra("HOUSE_POSITION", -1)

        val intent = Intent().apply {
            putExtra("LIKE_STATUS", sharedViewModel.favoriteHouseItems.value?.get(clickedHouse))
            Log.d("Clicked house position", position.toString())
            putExtra("HOME_POSITION", position)
        }
        setResult(Activity.RESULT_OK, intent)
        super.onBackPressed()

    }
}