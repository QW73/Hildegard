package com.qw73.hildegard.screens.main.home.exp

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle

import androidx.lifecycle.ViewModel
import com.qw73.hildegard.data.bd.Dish
import com.qw73.hildegard.data.bd.DishDao
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class ExpDishViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dishDao: DishDao,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {


    private val COUNT_KEY = "count"
    private val _count = MutableLiveData<Int>()

    val count: LiveData<Int>
        get() = _count

    init {
        _count.value = savedStateHandle.get<Int>(COUNT_KEY) ?: 0
    }

    fun incrementCount() {
        _count.value = (_count.value ?: 0) + 1
    }

    fun decrementCount() {
        val currentCount = _count.value ?: 0
        if (currentCount > 0) {
            _count.value = currentCount - 1
        }
    }

    fun resetCount() {
        _count.value = 0
    }

    fun getDishById(dishId: Long): Dish? {
        return runBlocking {
            return@runBlocking withContext(Dispatchers.IO) {
                dishDao.getDishById(dishId)
            }
        }
    }

    fun saveCount() {
        savedStateHandle.set(COUNT_KEY, _count.value)
    }

    fun restoreCount() {
        _count.value = savedStateHandle.get<Int>(COUNT_KEY) ?: 0
    }

    fun setCount(count: Int) {
        _count.value = count
    }

}