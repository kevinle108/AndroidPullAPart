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

    @Headers(
        "authority", "inventoryservice.pullapart.com",
        "accept", "*/*",
        "accept-language", "en-US,en;q=0.9",
        "content-type", "application/json; charset=utf-8",
        "origin", "https://www.pullapart.com",
        "referer", "https://www.pullapart.com/",
        "sec-ch-ua", "\"Google Chrome\";v=\"111\", \"Not(A:Brand\";v=\"8\", \"Chromium\";v=\"111\"",
        "sec-ch-ua-mobile", "?0",
        "sec-ch-ua-platform", "\"Windows\"",
        "sec-fetch-dest", "empty",
        "sec-fetch-mode", "cors",
        "sec-fetch-site", "same-site",
        "user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36",
    )
    @GET("Model")
    suspend fun getModelsFromMakeId(
        @Query("makeID") id: Int
    ) : List<CarModel>

    @Headers(
        "authority", "inventoryservice.pullapart.com",
        "accept", "*/*",
        "accept-language", "en-US,en;q=0.9",
        "content-type", "application/json; charset=utf-8",
        "origin", "https://www.pullapart.com",
        "referer", "https://www.pullapart.com/",
        "sec-ch-ua", "\"Google Chrome\";v=\"111\", \"Not(A:Brand\";v=\"8\", \"Chromium\";v=\"111\"",
        "sec-ch-ua-mobile", "?0",
        "sec-ch-ua-platform", "\"Windows\"",
        "sec-fetch-dest", "empty",
        "sec-fetch-mode", "cors",
        "sec-fetch-site", "same-site",
        "user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36",
    )
    @POST("Vehicle/Search")
    suspend fun vehicleSearch(
        @Body requestBody: Search
    ) : Response<List<SearchResult>>

}