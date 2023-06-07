package com.qw73.hildegard.screens.main.cart.orderDetails

import androidx.lifecycle.ViewModel
import com.qw73.hildegard.data.bd.dish.Dish
import com.qw73.hildegard.data.bd.dish.DishDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(
    private val dishDao: DishDao,
) : ViewModel() {


}