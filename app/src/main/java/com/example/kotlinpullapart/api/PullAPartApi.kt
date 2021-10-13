package com.example.kotlinpullapart.api

import com.example.kotlinpullapart.models.CarMake
import com.example.kotlinpullapart.models.CarModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import com.example.kotlinpullapart.models.User




interface PullAPartApi {
    //TODO: implement this interface for car makes

    @GET("Make")
    suspend fun getMakes() : List<CarMake>

//    @GET("Model?makeID={id}")
//    suspend fun getModelsFromMakeId(@Path("id") makeId: Int) : List<CarModel>

    @GET("Model")
    suspend fun getModelsFromMakeId(
        @Query("makeID") id: Int
    ) : List<CarModel>
}