package com.example.tangestate

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestHeaders
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.android.material.search.SearchBar
import okhttp3.Headers
import org.json.JSONException

private const val TAG = "SearchFragment"
private const val API_KEY = BuildConfig.API_KEY

class SearchFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var searchBar : SearchView
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
    private lateinit var houseTypeText : String
    private lateinit var houseStatus : Spinner
    private lateinit var houseStatusText : String
    private lateinit var garageStatus : CheckBox
    private lateinit var poolStatus : CheckBox
    private lateinit var housesAdapter : SearchHousesAdapter
    private lateinit var housesRv : RecyclerView
    private var houseItems = mutableListOf<House>()
    private lateinit var sharedViewModel : SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_browse, container, false)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        setupFilters(view)
        filterButtonClicked(view)

        val layoutManager = LinearLayoutManager(context)
        housesRv = view.findViewById(R.id.browse_houses_rv)
        housesRv.layoutManager = layoutManager
        housesRv.setHasFixedSize(true)
        housesAdapter = SearchHousesAdapter(view.context, houseItems, sharedViewModel)
        housesRv.adapter = housesAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchHouses("")

        searchBar.clearFocus()
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query != null) {
                    houseItems.clear()
                    fetchHouses(query)
                }
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                return true
            }

        })

    }

    companion object {
        @JvmStatic
        fun newInstance() : SearchFragment {
            return SearchFragment()
        }
    }

    private fun setupFilters(view: View) {
        searchBar = view.findViewById(R.id.browse_screen_searchbar)
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

        houseTypeText = ""
        houseStatusText = ""
    }

    private fun filterButtonClicked(view: View) {
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

                houseStatus.onItemSelectedListener = this
                houseType.onItemSelectedListener = this
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
    }

    private fun fetchHouses(searchText : String) {
        val client = AsyncHttpClient()
        var params = RequestParams()
        var headers = RequestHeaders()
        //params["X-RapidAPI-Key"] = API_KEY
        headers["x-rapidapi-key"] = API_KEY
        headers["x-rapidapi-host"] = "zillow-com1.p.rapidapi.com"

        var clientUrl = "https://zillow-com1.p.rapidapi.com/propertyExtendedSearch"

        params["page"] = "1"
        if(searchText != "") {
            params["location"] = searchText
        }
        else {
            params["location"] = "Cupertino, CA"
        }

        if(!(minPrice.text.toString().isNullOrBlank() and maxPrice.text.toString().isNullOrBlank() and
                    maxBeds.text.toString().isNullOrBlank() and minBeds.text.toString().isNullOrBlank() and
                    maxBaths.text.toString().isNullOrBlank() and minBaths.text.toString().isNullOrBlank() and
                    maxSqft.text.toString().isNullOrBlank() and minSqft.text.toString().isNullOrBlank())) {

            params["location"] = searchText
            params["home_type"] = houseTypeText
            params["status_type"] = houseStatusText
            params["minPrice"] = minPrice.text.toString()
            params["maxPrice"] = maxPrice.text.toString()
            params["bathsMin"] = minBaths.text.toString()
            params["bathsMax"] = maxBaths.text.toString()
            params["bedsMin"] = minBeds.text.toString()
            params["bedsMax"] = maxBeds.text.toString()
            params["sqftMin"] = minSqft.text.toString()
            params["sqftMax"] = maxSqft.text.toString()

            if(garageStatus.isChecked) {
                params["hasGarage"] = "True"
            }
            if(poolStatus.isChecked) {
                params["hasPool"] = "True"
            }
        }

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

                // properties -> props -> items -> properties

                try {
                    val parsedJson = createJson().decodeFromString(
                        BaseResponse.serializer(),
                        json.jsonObject.toString()
                    )
                    parsedJson.properties?.let { list ->
                        houseItems.addAll(list)
                        housesAdapter.notifyDataSetChanged()
                    }
                } catch (e: JSONException) {
                    Log.e(TAG, "Exception: $e")
                }
            }
        })


    }

    override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, l: Long) {
        if (adapterView != null) {
            when(adapterView.id) {
                R.id.home_status_spinner -> houseStatusText = adapterView.getItemAtPosition(position).toString().replace("\\s".toRegex(), "")
                R.id.building_type_spinner -> houseTypeText = adapterView.getItemAtPosition(position).toString().replace("\\s".toRegex(), "")

            }

        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}
