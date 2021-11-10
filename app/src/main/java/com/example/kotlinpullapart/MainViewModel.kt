package com.example.kotlinpullapart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinpullapart.api.RetrofitInstance
import com.example.kotlinpullapart.models.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

private const val TAG = "MainViewModel"
class MainViewModel: ViewModel() {

    private var makes = listOf<CarMake>()
    private var makeToModelsMap = mutableMapOf<Int, List<CarModel>>()
    private var lotResults = listOf<LotItem>()

    fun getLotResults(): List<LotItem> {
        return lotResults
    }

    fun updateLotResults(newResults: List<LotItem>): List<LotItem> {
        lotResults = lotResults + newResults
        lotResults = lotResults.sortedBy { it.row}
        return lotResults
    }

    fun getMakes() {
        viewModelScope.launch{
            val fetchedPost = RetrofitInstance.api.getMakes()
            makes = fetchedPost.filter { !it.rareFind }
            getModels()
        }
    }

    fun getModelsFromMakeId(id: Int): List<CarModel> {
        var carModels = listOf<CarModel>()
        runBlocking {
            val response = RetrofitInstance.api.getModelsFromMakeId(id)
            println(response)
            carModels = response
            Log.i(TAG, "getModelsFromMakeId: ${carModels}")
        }
        return carModels
    }

    fun getModels() {
        if (makes.size > 0) {
            var fetchedModels = listOf<CarModel>()
            viewModelScope.launch {
                for (make: CarMake in makes) {
                    fetchedModels = RetrofitInstance.api.getModelsFromMakeId(make.makeID)
                    makeToModelsMap.put(make.makeID, fetchedModels)
                }
            }
        }
    }



    fun searchCar(
        year: String,
        make: Int,
        model: String
    ): List<LotItem> {
        var lotLocations = listOf<LotItem>()
        runBlocking {
            val search = Search(
                listOf("8"),
                listOf(model),
                make,
                listOf(year)
            )
//            val body = "{Locations: [8], MakeID: 56, Years: [2000], Models: [861]}"
            Log.i(TAG, "request body: ${search}")
            val response = RetrofitInstance.api.vehicleSearch(search)
            if (response.isSuccessful) {
                Log.i(TAG, "Response: $response")
                Log.i(TAG, "Code: ${response.code()}")
                Log.i(TAG, "Body: ${response.body()}")
                val searchResult: SearchResult = response.body()!![0]
                Log.i(TAG, "searchResult location id: ${searchResult.locationID}")
                Log.i(TAG, "searchResult exact: ${searchResult.exact.size}")
                lotLocations = searchResult.exact
            } else {
                Log.i(TAG, "Failed request")
                Log.i(TAG, "Code: ${response.code()}")
                Log.i(TAG, "Body: ${response.body()}")
                Log.i(TAG, "Message: ${response.message()}")
                Log.i(TAG, "ErrorBody: ${response.errorBody()}")
                Log.i(TAG, "Headers: ${response.headers()}")
            }
        }
        return lotLocations
    }
}
