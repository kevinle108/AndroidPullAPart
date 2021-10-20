package com.example.kotlinpullapart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinpullapart.api.RetrofitInstance
import com.example.kotlinpullapart.models.CarMake
import com.example.kotlinpullapart.models.CarModel
import com.example.kotlinpullapart.models.Search
import kotlinx.coroutines.launch
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
            makes = fetchedPost


//            Log.i(TAG, "models list size after: ${models.size}")


//            getModels()
            searchCar()
        }
    }

    fun getModels() {
        if (makes.size > 0) {
            var fetchedModels = listOf<CarModel>()
            viewModelScope.launch {
                for (make: CarMake in makes) {
                    fetchedModels = RetrofitInstance.api.getModelsFromMakeId(make.makeID)
                    makeToModelsMap.put(make.makeID, fetchedModels)
                }
                Log.i(TAG, "Done fetching models for ${makeToModelsMap.size} makes")
                for (make in makeToModelsMap) {
                    Log.i(TAG, "Make ${make.key}:")
                    Log.i(TAG, "     Models: ${make.value.size}")
                }

//                Log.i(TAG, "TEST TOYOTA lookup")
//                val toyotaModels: List<CarModel> = makeToModelsMap[56]!!.sortedBy { it.modelName }
//                for (toyoModel in toyotaModels) {
//                    Log.i(TAG, "   ${toyoModel.modelName}")
//                }

            }
        }

    }

    fun searchCar() {
        // sample data required for car search: {"Locations":["8"],"Models":["861"],"MakeID":56,"Years":[]}
//        const makeName = carMakeSelect[carMakeSelect.selectedIndex].text;
//        const modelName = carModelSelect[carModelSelect.selectedIndex].text;
//        let _data = {
//            Locations: [locationID],
//            Models: [carModelSelect.value],
//            MakeID: carMakeSelect.value,
//            Years: [carYearSelect.value],
//        };
//        fetch(searchURL, {
//            method: "POST",
//            headers: { "Content-Type": "application/json; charset=UTF-8" },
//            body: JSON.stringify(_data),
//        })


        viewModelScope.launch {
//            val body = Search(listOf(8), listOf(861), 56, listOf(2000))
            val search = Search(
                listOf("8"),
                listOf("861"),
                "56",
                listOf("2000")
            )


//            val body = JSONObject()
//            jsonBody.put("location", listOf(8))
//            jsonBody.put("models", 56)
//            jsonBody.put("makeID", listOf(8))
//            jsonBody.put("years", listOf(2000))

//            val body = "{Locations: [8], MakeID: 56, Years: [2000], Models: [861]}"
            Log.i(TAG, "request body: ${search}")
            val response = RetrofitInstance.api.vehicleSearch(search)
            if (response.isSuccessful) {
                Log.i(TAG, "Response: $response")
            } else {
                Log.i(TAG, "Failed request")
                Log.i(TAG, "Code: ${response.code()}")
                Log.i(TAG, "Body: ${response.body()}")
                Log.i(TAG, "Message: ${response.message()}")
                Log.i(TAG, "ErrorBody: ${response.errorBody()}")
                Log.i(TAG, "Headers: ${response.headers()}")

            }
        }




    }
}

//fun rawJSON() {
//
//    // Create Retrofit
//    val retrofit = Retrofit.Builder()
//        .baseUrl("http://dummy.restapiexample.com")
//        .build()
//
//    // Create Service
//    val service = retrofit.create(APIService::class.java)
//
//    // Create JSON using JSONObject
//    val jsonObject = JSONObject()
//    jsonObject.put("name", "Jack")
//    jsonObject.put("salary", "3540")
//    jsonObject.put("age", "23")
//
//    // Convert JSONObject to String
//    val jsonObjectString = jsonObject.toString()
//
//    // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
//    val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
//
//    CoroutineScope(Dispatchers.IO).launch {
//        // Do the POST request and get response
//        val response = service.createEmployee(requestBody)
//
//        withContext(Dispatchers.Main) {
//            if (response.isSuccessful) {
//
//                // Convert raw JSON to pretty JSON using GSON library
//                val gson = GsonBuilder().setPrettyPrinting().create()
//                val prettyJson = gson.toJson(
//                    JsonParser.parseString(
//                        response.body()
//                            ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
//                    )
//                )
//
//                Log.d("Pretty Printed JSON :", prettyJson)
//
//            } else {
//
//                Log.e("RETROFIT_ERROR", response.code().toString())
//
//            }
//        }
//    }
//}