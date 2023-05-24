package com.qw73.hildegard.screens.main.home


import android.content.Context
import androidx.lifecycle.*
import com.qw73.hildegard.screens.main.SharedViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {


    private val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name
}