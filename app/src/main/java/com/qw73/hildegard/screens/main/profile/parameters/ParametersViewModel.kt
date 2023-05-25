package com.qw73.hildegard.screens.main.profile.parameters


import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject


@HiltViewModel
class ParametersViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _isDataFull = MutableLiveData<Boolean>()
    val isDataFull: LiveData<Boolean> get() = _isDataFull

    fun setIsDataFull(isDataFull: Boolean) {
        _isDataFull.value = isDataFull
    }

    fun calculateAge(birthday: Date): Int {
        val calendar = Calendar.getInstance()

        val currentDate = calendar.time
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        calendar.time = birthday
        val birthYear = calendar.get(Calendar.YEAR)
        val birthMonth = calendar.get(Calendar.MONTH)
        val birthDay = calendar.get(Calendar.DAY_OF_MONTH)

        var age = currentYear - birthYear

        // Проверяем, не наступил ли день рождения в текущем году
        if (currentMonth < birthMonth || (currentMonth == birthMonth && currentDay < birthDay)) {
            age--
        }

        return age
    }

}