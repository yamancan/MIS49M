package com.boun.bounyemekhane.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Recipe(
    @PrimaryKey val recipeId: Int,
    @ColumnInfo(name = "dayIndex") val dayInx: Byte,
    @ColumnInfo(name = "recipeName") val recipeName: String,
    @ColumnInfo(name = "recipe") val recipe: String
)
