package com.example.kotlinpullapart.models

import java.io.Serializable

data class LotLocation(
    val lots: List<LotItem>
) : Serializable