package com.example.kotlinpullapart.data

import com.example.kotlinpullapart.models.CarMake
import com.example.kotlinpullapart.models.CarModel

const val START_YEAR = 1955
const val END_YEAR = 2021


object StartUp {
    val years: List<Int> = yearRange(START_YEAR, END_YEAR)
    val makes: List<CarMake> = mutableListOf(
//        CarMake(0, "MAKE", false),
        CarMake(6, "ACURA", false),
        CarMake(8, "AMC", false),
        CarMake(11, "AUDI", false),
        CarMake(13, "BMW", false),
        CarMake(14, "BUICK", false),
        CarMake(15, "CADILLAC", false),
        CarMake(16, "CHEVROLET", false),
        CarMake(17, "CHRYSLER", false),
        CarMake(18, "DAEWOO", false),
        CarMake(19, "DAIHATSU", false),
        CarMake(20, "DATSUN", false),
        CarMake(21, "DODGE", false),
        CarMake(22, "EAGLE", false),
        CarMake(23, "FIAT", false),
        CarMake(24, "FORD", false),
        CarMake(25, "GEO", false),
        CarMake(26, "GMC", false),
        CarMake(27, "HONDA", false),
        CarMake(28, "HYUNDAI", false),
        CarMake(29, "INFINITI", false),
        CarMake(31, "ISUZU", false),
        CarMake(32, "JAGUAR", false),
        CarMake(33, "JEEP", false),
        CarMake(35, "LINCOLN", false),
        CarMake(36, "MAZDA", false),
        CarMake(37, "MERCEDES-BENZ", false),
        CarMake(38, "MERCURY", false),
        CarMake(40, "MITSUBISHI", false),
        CarMake(41, "NISSAN", false),
        CarMake(42, "OLDSMOBILE", false),
        CarMake(46, "PLYMOUTH", false),
        CarMake(47, "PONTIAC", false),
        CarMake(48, "PORSCHE", false),
        CarMake(51, "SAAB", false),
        CarMake(52, "SATURN", false),
        CarMake(54, "SUBARU", false),
        CarMake(55, "SUZUKI", false),
        CarMake(56, "TOYOTA", false),
        CarMake(58, "VOLVO", false),
        CarMake(59, "VW", false),
        CarMake(61, "KIA", false),
        CarMake(67, "LEXUS", false),
        CarMake(69, "LAND ROVER", false),
        CarMake(97, "MINI", false),
        CarMake(100, "SCION", false),
        CarMake(101, "SMART", false),
        CarMake(123, "RAM", false),
        CarMake(128, "GENESIS", false),
        CarMake(133, "RIVIAN", false),
        CarMake(134, "WAGONEER", false),
        CarMake(135, "LUCID", false)
    )

    fun yearRange(start: Int, end: Int): List<Int> {
        var years = mutableListOf<Int>()
        for (i in start..end) {
            years.add(i)
        }
        println("years ${years.size}")
        return years
    }
}