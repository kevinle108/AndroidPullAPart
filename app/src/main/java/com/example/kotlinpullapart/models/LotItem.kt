package com.example.kotlinpullapart.models

data class LotItem(
    val row: Int,
    val locID: Int,
    val locName: String,
    val makeID: Int,
    val makeName: String,
    val modelID: Int,
    val modelName: String,
    val dateYardOn: String,
    val vin: String,
)

//{
//    "vinID": 1299691,
//    "ticketID": 1271235,
//    "lineID": 1,
//    "locID": 8,
//    "locName": "Louisville",
//    "makeID": 56,
//    "makeName": "TOYOTA",
//    "modelID": 861,
//    "modelName": "SIENNA",
//    "modelYear": 2000,
//    "row": 310,
//    "vin": "4T3ZF13C4YU250625",
//    "dateYardOn": "2021-10-11T17:08:02.08",
//    "vinDecodedId": null,
//    "extendedInfo": null
//},