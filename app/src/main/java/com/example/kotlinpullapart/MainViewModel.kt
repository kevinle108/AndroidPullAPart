package com.example.kotlinpullapart

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinpullapart.api.RetrofitInstance
import com.example.kotlinpullapart.data.StartUp
import com.example.kotlinpullapart.models.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

private const val TAG = "MainViewModel"
class MainViewModel: ViewModel() {



    val years = StartUp.years
    val makes = StartUp.makes.sortedBy { it.makeName }
    private var makeToModelsMap = mutableMapOf<Int, List<CarModel>>()
    private var modelsNameIdMap = mutableMapOf<String, Int>()

    private var lotResults = listOf<LotItem>()
    private var searchesToItemsMap = mutableMapOf<String, Int>()

    private var beforeSelection = true
    private var selectedYear = 0
    private var selectedMakeId = 0
    private var selectedModelId = 0

    private var currentModelsList = mutableListOf<String>()


    fun getLotResults(): List<LotItem> = lotResults

    fun getBeforeSelection(): Boolean = beforeSelection

    fun setBeforeSelection(value: Boolean) = { beforeSelection = value }

    fun updateLotResults(newResults: List<LotItem>): List<LotItem> {
        lotResults = lotResults + newResults
        lotResults = lotResults.sortedBy { it.row}
        return lotResults
    }

    fun getSelectedYear(): Int = selectedYear
//    fun getSelectedMakeId(): Int = selectedMakeId
//    fun getSelectedModelId(): Int = selectedModelId
    fun getCurrentModelsList(): List<String> = currentModelsList
    fun currentSearchEntry(): String {
        val year: String = selectedYear.toString()
        val make: String = makes.first{ it.makeID == selectedMakeId }.makeName
        val model: String = modelsNameIdMap.filterValues { it == selectedMakeId }.keys.first()
        return "$year $make $model"
    }


    fun isDuplicateSearch(searchEntry: String): Boolean {
        return searchesToItemsMap.containsKey(searchEntry)
    }

    fun yearSelectionHandler(position: Int) {
        selectedYear = years[position]
    }

    fun makeSelectionHandler(position: Int) : List<String> {
        selectedMakeId = makes[position].makeID
        val models: List<CarModel> = getModelsFromMakeId(selectedMakeId)
        println(models)
        val modelNames: List<String> = models.map { it.modelName }
        currentModelsList = modelNames.sorted().toMutableList()
        for (model in models) {
            if (modelsNameIdMap[model.modelName] == null) {
                modelsNameIdMap.put(model.modelName, model.modelID)
            }
        }
        return currentModelsList
    }

    fun modelSelectionHandler(position: Int) {
        val selectedModelName = currentModelsList[position]
        selectedModelId = modelsNameIdMap[selectedModelName] ?: 0
    }

//    fun getMakes() {
//        viewModelScope.launch{
//            val fetchedPost = RetrofitInstance.api.getMakes()
//            makes = fetchedPost.filter { !it.rareFind }
//            getModels()
//        }
//    }

    fun getModelsFromMakeId(id: Int): List<CarModel> {
        var carModels = listOf<CarModel>()
        runBlocking {
            val response = RetrofitInstance.api.getModelsFromMakeId(id)
            carModels = response
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
            val response = RetrofitInstance.api.vehicleSearch(search)
            if (response.isSuccessful) {
                val searchResult: SearchResult = response.body()!![0]
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
