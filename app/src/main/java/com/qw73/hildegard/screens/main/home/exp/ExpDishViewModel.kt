package com.qw73.hildegard.screens.main.home.exp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qw73.hildegard.data.bd.dish.Dish
import com.qw73.hildegard.data.bd.dish.DishDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ExpDishViewModel @Inject constructor(
    private val dishDao: DishDao,
) : ViewModel() {

    private val dishLiveDataMap: MutableMap<Long, MutableLiveData<Dish>> = mutableMapOf()

    fun getDishById(dishId: Long): Dish? {
        return runBlocking {
            return@runBlocking withContext(Dispatchers.IO) {
                dishDao.getDishById(dishId)
            }
        }
    }

    private fun updateDishLiveData(dishId: Long, dish: Dish) {
        if (dishLiveDataMap.containsKey(dishId)) {
            dishLiveDataMap[dishId]?.postValue(dish)
        }
    }

    fun getDishFlow(dishId: Long): Flow<Dish?> {
        return dishDao.getDishFlow(dishId)
    }

    fun updateDishCount(dishId: Long, newCount: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dishDao.updateDishCount(dishId, newCount)
            val updatedDish = dishDao.getDishById(dishId)
            updatedDish?.let {
                updateDishLiveData(dishId, it)
            }
        }
    }
}
