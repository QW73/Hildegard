package com.qw73.hildegard.data.bd.dish

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

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

    @Query("SELECT * FROM dishes WHERE id = :dishId")
    suspend fun getDishById(dishId: Long): Dish?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDishes(dishes: List<Dish>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrders(orders: Order)

    @Query("DELETE FROM dishes")
    suspend fun clearTable()

    @Query("SELECT COUNT(*) FROM dishes WHERE id = :dishId")
    suspend fun getDishCount(dishId: Long): Int?

    @Query("DELETE FROM sqlite_sequence WHERE name='dishes'")
    suspend fun resetId()

    @Query("UPDATE dishes SET count = :count WHERE id = :dishId")
    suspend fun updateDishCount(dishId: Long, count: Int)

    @Query("SELECT * FROM dishes WHERE count != 0")
    suspend fun getDishForCart(): List<Dish>

    @Query("SELECT * FROM dishes WHERE id = :dishId")
    fun getDishLiveData(dishId: Long): LiveData<Dish>

    @Query("SELECT * FROM dishes WHERE id = :dishId")
    fun getDishFlow(dishId: Long): Flow<Dish?>
}
