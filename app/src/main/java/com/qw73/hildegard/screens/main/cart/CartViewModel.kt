package com.qw73.hildegard.screens.main.cart

import android.content.Context
import androidx.lifecycle.ViewModel
import com.qw73.hildegard.data.bd.dish.Dish
import com.qw73.hildegard.data.bd.dish.DishDao
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dishDao: DishDao,
) : ViewModel() {

    suspend fun getDishForCart(): List<Dish> {
        return dishDao.getDishForCart()
    }

}