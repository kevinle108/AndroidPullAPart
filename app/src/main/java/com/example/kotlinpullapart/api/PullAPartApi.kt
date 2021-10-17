package com.example.kotlinpullapart.api

import com.example.kotlinpullapart.models.CarMake
import com.example.kotlinpullapart.models.CarModel
import com.example.kotlinpullapart.models.SearchResult
import com.example.kotlinpullapart.models.User
import okhttp3.RequestBody
import retrofit2.http.*


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

    @POST("Vehicle/Search")
    suspend fun vehicleSearch(@Body requestBody: RequestBody) : List<SearchResult>

}