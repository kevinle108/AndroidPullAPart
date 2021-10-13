package com.example.kotlinpullapart.models

//{
//    "modelID": 2,
//    "modelName": "CL",
//    "makeID": 6,
//    "showWebsite": true,
//    "showTouchscreen": true,
//    "dateModified": "2021-10-12T02:00:00",
//    "dateCreated": "2001-12-21T11:30:13"
//},
data class CarModel(
    val modelID: Int,
    val modelName: String,
    val makeID: Int,
    val showWebsite: Boolean,
    val showTouchscreen: Boolean,
)
