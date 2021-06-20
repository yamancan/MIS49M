package com.boun.bounyemekhane.models

data class Food(
    val food_date: String, // yyyy-MM-dd
    val food_time_index: Int, // (Öğle = 0, Akşam = 1)
    val soup: String,
    val main: String,
    val vegetarian: String,
    val auxiliary: String,
    val snack: String
)
