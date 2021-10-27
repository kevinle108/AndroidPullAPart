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

    fun getMakes() {
        viewModelScope.launch{
            val fetchedPost = RetrofitInstance.api.getMakes()
//            Log.i(TAG, "Fetched makes: $fetchedPost")
//            Log.i(TAG, "# of CarMakes: ${fetchedPost.size}")
//            Log.i(TAG, "models list size before: ${makeToModelsMap.size}")
            makes = fetchedPost.filter { !it.rareFind }


//            Log.i(TAG, "models list size after: ${models.size}")


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
//                Log.i(TAG, "Done fetching models for ${makeToModelsMap.size} makes")
//                for (make in makeToModelsMap) {
//                    Log.i(TAG, "Make ${make.key}:")
////                    Log.i(TAG, "     Models: ${make.value.size}")
//                    for (model in make.value) {
//                        Log.i(TAG, "    ModelID: ${model.modelID}")
//                    }
//                }
//                val filtered56 = makes.filter { it.makeID == 56}
//                val filtered56 = makes.first { it.makeID == 56}
//                Log.i(TAG, "getModels: ${makeToModelsMap[56]}")
//                Log.i(TAG, "getModels: Make: ${filtered56.makeName}")
//                val filtered861 = makeToModelsMap[56]!!.first { it.modelID == 861 }
//                Log.i(TAG, "getModels: Model: ${filtered861.modelName}")
//                val commonMakes = makes.filter { !(it.rareFind) }
                var text = ""
                for (make in makeToModelsMap) {
                    var makeText = ""
//                    makeText += "${make.value}"
                    for (model in make.value) {
                        makeText += "${model.modelID} to \"${model.modelName}\", "
                    }
//                    makeText += "), "
                    text += makeText
                }
//                text = text.replace("[","")
//                text = text.replace("]","")

//                println("ENDNASDFMNSDGMDFLOGMDFLG")
//                Log.i(TAG, "getModels: HELLO THERE")
//                println("Text length = ${text.length}")

                println(text.substring(0,3000))
                println(text.substring(3000,6000))
                println(text.substring(6000,9000))
                println(text.substring(9000,12000))
                println(text.substring(12000,15000))
                println(text.substring(15000,18000))
                println(text.substring(18000,21000))
                println(text.substring(21000,24000))
                println(text.substring(24000,27000))
                println(text.substring(27000,30000))
                println(text.substring(30000,text.length))


//                println(text.substring(30000,33000))
//                println(text.substring(33000,36000))
//                println(text.substring(36000,text.length))

//                println("print statement: $makes")




//                Log.i(TAG, "TEST TOYOTA lookup")
//                val toyotaModels: List<CarModel> = makeToModelsMap[56]!!.sortedBy { it.modelName }
//                for (toyoModel in toyotaModels) {
//                    Log.i(TAG, "   ${toyoModel.modelName}")
//                }

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
