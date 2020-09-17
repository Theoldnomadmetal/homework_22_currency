package com.example.homework_22_currency.data.model

import com.google.gson.JsonObject

data class CurrencyModel(
    val success : Boolean,
    val timestamp : Int,
    val base : String,
    val date : String,
    val rates : JsonObject
)