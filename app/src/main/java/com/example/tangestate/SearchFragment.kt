package com.example.tangestate

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

private const val TAG = "SearchFragment"
private const val API_KEY = "48138f1df4mshc65c2a624afd2dep14c5f9jsnc8d3268a92de"

class SearchFragment : Fragment() {
    private lateinit var filterText : TextView
    private lateinit var minPrice : EditText
    private lateinit var maxPrice : EditText
    private lateinit var minBeds : EditText
    private lateinit var maxBeds : EditText
    private lateinit var minBaths : EditText
    private lateinit var maxBaths : EditText
    private lateinit var minSqft : EditText
    private lateinit var maxSqft : EditText
    private lateinit var houseType : Spinner
    private lateinit var houseStatus : Spinner
    private lateinit var garageStatus : CheckBox
    private lateinit var poolStatus : CheckBox
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_browse, container, false)

        filterText = view.findViewById(R.id.filter_text)

        minPrice = view.findViewById(R.id.minPrice_editText)
        maxPrice = view.findViewById(R.id.maxPrice_editText)
        minBeds = view.findViewById(R.id.minBeds_editText)
        maxBeds = view.findViewById(R.id.maxBeds_editText)
        minBaths = view.findViewById(R.id.minBaths_editText)
        maxBaths = view.findViewById(R.id.maxBaths_editText)
        minSqft = view.findViewById(R.id.minSqft_editText)
        maxSqft = view.findViewById(R.id.maxSqft_editText)
        houseType = view.findViewById(R.id.building_type_spinner)
        houseStatus = view.findViewById(R.id.home_status_spinner)
        garageStatus = view.findViewById(R.id.hasGarage_checkbox)
        poolStatus = view.findViewById(R.id.hasPool_checkbox)

        filterText.setOnClickListener {
            if(filterText.text == "SHOW FILTERS") {
                filterText.text = "HIDE FILTERS"

                minPrice.visibility = View.VISIBLE
                maxPrice.visibility = View.VISIBLE
                minBeds.visibility = View.VISIBLE
                maxBeds.visibility = View.VISIBLE
                minBaths.visibility = View.VISIBLE
                maxBaths.visibility = View.VISIBLE
                minSqft.visibility = View.VISIBLE
                maxSqft.visibility = View.VISIBLE
                houseStatus.visibility = View.VISIBLE
                houseType.visibility = View.VISIBLE
                garageStatus.visibility = View.VISIBLE
                poolStatus.visibility = View.VISIBLE

                ArrayAdapter.createFromResource(
                    view.context,
                    R.array.building_type_items,
                    android.R.layout.simple_spinner_item
                ).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    houseType.adapter = adapter
                }
                ArrayAdapter.createFromResource(
                    view.context,
                    R.array.home_status_items,
                    android.R.layout.simple_spinner_item
                ).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    houseStatus.adapter = adapter
                }
            }
            else {
                filterText.text = "SHOW FILTERS"

                minPrice.visibility = View.INVISIBLE
                maxPrice.visibility = View.INVISIBLE
                maxPrice.visibility = View.INVISIBLE
                minBeds.visibility = View.INVISIBLE
                maxBeds.visibility = View.INVISIBLE
                minBaths.visibility = View.INVISIBLE
                maxBaths.visibility = View.INVISIBLE
                minSqft.visibility = View.INVISIBLE
                maxSqft.visibility = View.INVISIBLE
                houseStatus.visibility = View.INVISIBLE
                houseType.visibility = View.INVISIBLE
                garageStatus.visibility = View.INVISIBLE
                poolStatus.visibility = View.INVISIBLE
            }
        }
        fetchHouses()
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() : SearchFragment {
            return SearchFragment()
        }
    }

    private fun fetchHouses() {
        val client = AsyncHttpClient()
        var params = RequestParams()
        //params["X-RapidAPI-Key"] = API_KEY
        //params["location"] = "houston, tx"

        client.get("https://zillow-com4.p.rapidapi.com/properties/search?location=Houston%2C%20TX?x_rapidapi_key=48138f1df4mshc65c2a624afd2dep14c5f9jsnc8d3268a92de", object: JsonHttpResponseHandler(){
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "onFailure $response")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.i(TAG, "onSuccess: JSON moovies data $json")

            }
        })
    }
}