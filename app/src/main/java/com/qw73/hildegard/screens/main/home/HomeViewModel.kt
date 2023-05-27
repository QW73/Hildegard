package com.qw73.hildegard.screens.main.home


import android.content.Context
import androidx.lifecycle.*
import com.qw73.hildegard.data.bd.Dish
import com.qw73.hildegard.data.bd.DishDao
import com.qw73.hildegard.screens.main.SharedViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dishDao: DishDao
) : ViewModel() {

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    val exclusions: MutableList<String> = mutableListOf("Exclusion 3")

    suspend fun getDishesByCategory(category: String): List<Dish> {
        return dishDao.getDishesByCategory(category)
    }

    suspend fun getDishesByCategoryWithExclusions(category: String, excs: MutableList<String>): List<Dish> {
        return dishDao.getDishesByCategoryWithExclusions(category, excs)
    }
}
