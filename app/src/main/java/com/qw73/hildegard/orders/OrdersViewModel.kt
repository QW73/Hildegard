package com.qw73.hildegard.orders

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : ViewModel() {

}