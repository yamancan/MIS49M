package com.boun.bounyemekhane.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.boun.bounyemekhane.models.Recipe

@Dao
interface RecipeDao {

    @Query("INSERT INTO recipe (dayIndex, recipeName, recipe) VALUES (:dayInx, :recipeNa, :rec)")
    fun AddRecipe(dayInx: Byte, recipeNa: String, rec: String)

    @Query("SELECT * FROM recipe WHERE dayIndex = :dayInx LIMIT 1")
    fun GetRecipe(dayInx: Byte) : Recipe
}