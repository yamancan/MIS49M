package com.boun.bounyemekhane.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.boun.bounyemekhane.dao.RecipeDao
import com.boun.bounyemekhane.models.Recipe

@Database(entities = [Recipe::class], version = 1)
abstract class RecipeDatabase : RoomDatabase(){

    abstract fun recipeDao() : RecipeDao

}