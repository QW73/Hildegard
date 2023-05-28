package com.qw73.hildegard.data.bd

import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface DishDao {

    @Query("SELECT * FROM dishes WHERE category IN (:category)")
    suspend fun getDishesByCategory(category: String): List<Dish>

    suspend fun getDishesByExclusions(category: String, exclusionList: List<String>?): List<Dish> {
        val allDishes = getDishesByCategory(category)
        val filteredDishes = mutableListOf<Dish>()

        exclusionList?.let {
            for (dish in allDishes) {
                var excludeDish = false
                for (exclusion in it) {
                    if (dish.exclusions.contains(exclusion)) {
                        excludeDish = true
                        break
                    }
                }
                if (!excludeDish) {
                    filteredDishes.add(dish)
                }
            }
        }

        return filteredDishes
    }

    @Query("SELECT * FROM dishes")
    suspend fun getDishes(): List<Dish>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDishes(dishes: List<Dish>)

    @Query("DELETE FROM dishes")
    suspend fun clearTable()

    @Query("DELETE FROM sqlite_sequence WHERE name='dishes'")
    suspend fun resetId()

}
