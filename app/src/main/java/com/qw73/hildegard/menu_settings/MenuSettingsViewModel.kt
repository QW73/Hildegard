package com.qw73.hildegard.menu_settings

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MenuSettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : ViewModel() {

}