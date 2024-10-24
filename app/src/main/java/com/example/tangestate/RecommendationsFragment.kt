package com.example.tangestate

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestHeaders
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException
import kotlin.math.ceil
import kotlin.math.floor

private const val TAG = "RecommendationsFragment"
private const val ACRES_TO_SQFT = 43560
private const val API_KEY = BuildConfig.API_KEY

class RecommendationsFragment : Fragment(), OnItemClickListener {
    private lateinit var sharedViewModel : SharedViewModel
    private lateinit var recommendationsRv : RecyclerView
    private lateinit var recommendationsAdapter : RecommendationsAdapter
    private var houseItems = mutableListOf<House>()

    private var avgPrice = 0
    private var avgBeds = 0
    private var avgBaths = 0.0F
    private var avgSqft = 0.0F

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_suggestions, container, false)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        val layoutManager = LinearLayoutManager(context)
        recommendationsRv = view.findViewById(R.id.recommendations_rv)
        recommendationsRv.layoutManager = layoutManager
        recommendationsRv.setHasFixedSize(true)
        recommendationsAdapter = RecommendationsAdapter(view.context, houseItems, sharedViewModel, this)
        recommendationsRv.adapter = recommendationsAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        calculateHouseStatistics()
        fetchHouses()
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        fun newInstance() : RecommendationsFragment {
            return RecommendationsFragment()
        }
    }

    private fun calculateHouseStatistics() {
        if(sharedViewModel.favoriteHouseItems.value!!.isNotEmpty()) {
            var sumPrice = 0
            var sumBeds = 0
            var sumBaths = 0.0F
            var sumSqft = 0.0F

            for(house in sharedViewModel.favoriteHouseItems.value!!.keys) {
                if(house != null) {
                    sumPrice += house.housePrice!!
                    sumBeds += house.houseBeds!!
                    sumBaths += house.houseBaths!!
                    sumSqft += if(house.houseAreaUnit == "sqft") {
                        house.houseSqft!!
                    } else {
                        house.houseSqft!!.times(ACRES_TO_SQFT)
                    }
                }
            }
            avgPrice = sumPrice / sharedViewModel.favoriteHouseItems.value!!.size
            avgBeds = sumBeds / sharedViewModel.favoriteHouseItems.value!!.size
            avgBaths = sumBaths / sharedViewModel.favoriteHouseItems.value!!.size
            avgSqft = sumSqft / sharedViewModel.favoriteHouseItems.value!!.size
        }
        else {
            Toast.makeText(context, "No houses liked, statistics set to default values", Toast.LENGTH_LONG).show()
            avgPrice = 500000
            avgBeds = 2
            avgBaths = 2.0F
            avgSqft = 2000F
        }
    }

    private fun fetchHouses() {
        val client = AsyncHttpClient()
        val params = RequestParams()
        val headers = RequestHeaders()

        headers["x-rapidapi-key"] = API_KEY
        headers["x-rapidapi-host"] = "zillow-com1.p.rapidapi.com"

        val clientUrl = "https://zillow-com1.p.rapidapi.com/propertyExtendedSearch"

        params["location"] = "New York, NY; Los Angeles, CA; Palo Alto, CA; Austin, TX; Naples, FA"
        params["minPrice"] = (avgPrice - avgPrice / 10).toString()
        params["maxPrice"] = (avgPrice + avgPrice / 10).toString()
        params["minBeds"] = avgBeds.toString()
        params["maxBeds"] = (avgBeds + 1).toString()
        params["minBaths"] = floor(avgBaths).toString()
        params["maxBaths"] = ceil(avgBaths).toString()
        params["minSqft"] = (avgSqft - avgSqft / 10.0F).toString()
        params["maxSqft"] = (avgSqft + avgSqft / 10.0F).toString()
        params["page"] = "1"

        client.get(clientUrl, headers, params, object: JsonHttpResponseHandler(){
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
                    val parsedJson = createJson().decodeFromString(
                        BaseResponse.serializer(),
                        json.jsonObject.toString()
                    )
                    if(parsedJson.properties!!.isEmpty()) {
                        Toast.makeText(context, "No houses were found with the specified filters", Toast.LENGTH_LONG).show()
                    }
                    parsedJson.properties.let { list ->
                        houseItems.addAll(list)
                        for(house in houseItems) {
                            if(sharedViewModel.favoriteHouseItems.value?.get(house) == true) {
                                houseItems.remove(house)
                            }
                        }
                        recommendationsAdapter.notifyDataSetChanged()
                    }

                } catch (e: JSONException) {
                    Log.e(TAG, "Exception: $e")
                }
            }
        })
    }

    private val REQUEST_CODE = 1001

    override fun onItemClick(house: House, likeStatus: Boolean?) {
        val intent = Intent(context, HouseDetailsActivity::class.java).apply {
            putExtra("HOUSE_EXTRA", house)
            putExtra("LIKE_STATUS", likeStatus)
            putExtra("HOUSE_POSITION", houseItems.indexOf(house))
        }
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val likeStatus = data?.getBooleanExtra("LIKE_STATUS", false)
            val position = data?.getIntExtra("HOME_POSITION", -1)
            Log.d("Clicked house data maintained", likeStatus.toString() + ", " + position.toString())
            if (position != null && likeStatus != null) {
                if(likeStatus) {
                    recommendationsAdapter.updateItemColor(position)
                }
            }
            else {
                error("Attempting to access invalid house index")
            }
        }
    }
}