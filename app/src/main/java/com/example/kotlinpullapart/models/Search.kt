package com.example.kotlinpullapart.models

import com.squareup.moshi.JsonClass

data class Search(
    val locations: List<String>,
    val models: List<String>,
    val makeID: Int,
    val years: List<String>
)
// "{Locations: [8], MakeID: 56, Years: [2000], Models: [861]}"
//            Locations: [locationID],
//            Models: [carModelSelect.value],
//            MakeID: carMakeSelect.value,
//            Years: [carYearSelect.value],