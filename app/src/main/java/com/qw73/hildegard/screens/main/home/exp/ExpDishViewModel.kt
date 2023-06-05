package com.qw73.hildegard.screens.main.home.exp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qw73.hildegard.data.bd.dish.Dish
import com.qw73.hildegard.data.bd.dish.DishDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

    fun getDishLiveData(dishId: Long): LiveData<Dish> {
        if (!dishLiveDataMap.containsKey(dishId)) {
            dishLiveDataMap[dishId] = MutableLiveData()
        }
        return dishLiveDataMap[dishId]!!
    }

    private fun updateDishLiveData(dishId: Long, dish: Dish) {
        if (dishLiveDataMap.containsKey(dishId)) {
            dishLiveDataMap[dishId]?.postValue(dish)
        }
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
