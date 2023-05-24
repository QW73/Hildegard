package com.qw73.hildegard.screens.main.profile

// import com.qw73.hildegard.data.OPEN_PERSONAL_DATA
import android.content.Context
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.qw73.hildegard.R
import com.qw73.hildegard.data.OPEN_PARAMETERS
import com.qw73.hildegard.data.OPEN_PERSONAL_DATA
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : ViewModel() {


    val profileEvents: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    fun profileItemClick(layout: LinearLayoutCompat) {
        profileEvents.value = when (layout.id) {
            R.id.container_personal_data -> OPEN_PERSONAL_DATA
            R.id.container_parameters -> OPEN_PARAMETERS
            // Добавьте другие условия для определения значений для других лейаутов
            else -> throw IllegalArgumentException("Unknown layout clicked")
        }
    }
}