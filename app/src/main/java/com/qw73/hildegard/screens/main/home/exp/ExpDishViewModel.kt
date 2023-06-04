package com.qw73.hildegard.screens.main.home.exp

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.qw73.hildegard.data.bd.Dish
import com.qw73.hildegard.data.bd.DishDao
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


@HiltViewModel
class ExpDishViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dishDao: DishDao,
) : ViewModel() {



}