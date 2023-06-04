package com.qw73.hildegard.screens.main.cart

import android.content.Context
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.qw73.hildegard.R
import com.qw73.hildegard.data.OPEN_MENU_SETTING
import com.qw73.hildegard.data.OPEN_ORDERS
import com.qw73.hildegard.data.OPEN_PARAMETERS
import com.qw73.hildegard.data.OPEN_PERSONAL_DATA
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : ViewModel() {

}