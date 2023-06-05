package com.qw73.hildegard.screens.main.home


import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.qw73.hildegard.data.bd.dish.Dish
import com.qw73.hildegard.data.bd.dish.DishDao
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dishDao: DishDao,
) : ViewModel() {

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    suspend fun getDishesByCategory(category: String): List<Dish> {
        return dishDao.getDishesByCategory(category)
    }

    suspend fun getDishesByExclusions(category: String,ex:List<String>?): List<Dish> {
        return dishDao.getDishesByExclusions(category,ex)
    }

}

