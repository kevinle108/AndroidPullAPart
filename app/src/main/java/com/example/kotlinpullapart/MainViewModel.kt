package com.example.kotlinpullapart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinpullapart.api.RetrofitInstance
import com.example.kotlinpullapart.models.CarMake
import com.example.kotlinpullapart.models.CarModel
import kotlinx.coroutines.launch
import java.util.*

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


            getModels()

            //TODO: API call for each make number to fetch a list of models
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
                Log.i(TAG, "TEST TOYOTA lookup")
                val toyotaModels: List<CarModel> = makeToModelsMap[56]!!.sortedBy { it.modelName }
                for (toyoModel in toyotaModels) {
                    Log.i(TAG, "   ${toyoModel.modelName}")
                }
            }
        }

    }
}