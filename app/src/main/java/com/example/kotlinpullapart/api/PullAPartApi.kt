package com.example.kotlinpullapart.api

import com.example.kotlinpullapart.models.*
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
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

    @Headers("Content-Type: application/json;charset=utf-8",)
    @POST("Vehicle/Search")
    suspend fun vehicleSearch(
        @Body requestBody: Search
    ) : Response<List<SearchResult>>

}