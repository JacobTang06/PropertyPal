package com.example.tangestate

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_house_details)

        setupHouseDetails()
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

    private fun displayHouseDetails() {
        val house = intent.getSerializableExtra("HOUSE_EXTRA") as House

        housePrice.text = house.housePrice.toString()
        houseBeds.text = house.houseBeds.toString()
        houseBaths.text = house.houseBaths.toString()
        houseSqft.text = house.houseSqft.toString()
        houseStatus.text = house.houseStatus
        houseAddress.text = "${house.houseAddress}, ${house.houseCity}, ${house.houseState} ${house.houseZipcode}"
        listingAgent.text = house.agentName
        listingCompany.text = house.companyName
        houseSummary.text = house.houseDescription

        for(fact in house.facts!!) {
            houseFacts.text = fact.factLabel + ": " + fact.factValue + "\n"
        }

        Glide.with(this@HouseDetailsActivity)
            .load(house.houseImageUrl)
            .centerInside()
            .into(houseImage)
    }
}