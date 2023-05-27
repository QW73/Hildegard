package com.qw73.hildegard.data.bd

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DishDao {
    @Query("SELECT * FROM dishes WHERE category IN (:category)")
    suspend fun getDishesByCategory(category: String): List<Dish>

    @Query("SELECT * FROM dishes WHERE category = :category AND exclusions NOT IN (:exclusions)")
    suspend fun getDishesByCategoryWithExclusions(category: String, exclusions: List<String>): List<Dish>

    @Query("SELECT * FROM dishes")
    suspend fun getDishes(): List<Dish>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDishes(dishes: List<Dish>)

    @Query("DELETE FROM dishes")
    suspend fun clearTable()

    @Query("DELETE FROM sqlite_sequence WHERE name='dishes'")
    suspend fun resetId()
}