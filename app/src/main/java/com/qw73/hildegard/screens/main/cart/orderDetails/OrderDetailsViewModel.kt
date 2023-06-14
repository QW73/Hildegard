package com.qw73.hildegard.screens.main.cart.orderDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.qw73.hildegard.data.bd.dish.Dish
import com.qw73.hildegard.data.bd.dish.DishDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(
    private val dishDao: DishDao,
) : ViewModel() {

    private val _orderDate = MutableLiveData<String>()
    val orderDate: LiveData<String> get() = _orderDate

    fun setOrderDate(orderDate: String) {
        _orderDate.value = orderDate
    }

    private val _orderTime = MutableLiveData<String>()
    val orderTime: LiveData<String> get() = _orderTime

    fun setOrderTime(orderTime: String) {
        _orderTime.value = orderTime
    }

    private val _orderWeeks = MutableLiveData<Int>()
    val orderWeeks: LiveData<Int> get() = _orderWeeks

    fun setOrderWeeks(orderWeeks: Int) {
        _orderWeeks.value = orderWeeks
    }

    suspend fun getDishForCart(): List<Dish> {
        return dishDao.getDishForCart()
    }

    suspend fun updateDishCount(dishId: Long, count: Int) {
            dishDao.updateDishCount(dishId, count)
    }
}