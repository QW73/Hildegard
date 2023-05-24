package com.qw73.hildegard.screens.main.profile.personalData

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class PersonalDataViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _phoneNumber = MutableLiveData<String>()
    val phoneNumber: LiveData<String> get() = _phoneNumber

    private val _birthday = MutableLiveData<String>()
    val birthday: LiveData<String> get() = _birthday

    private val _isValidEmail = MutableLiveData<Boolean>()
    val isValidEmail: LiveData<Boolean> get() = _isValidEmail

    fun setPhoneNumber(phoneNumber: String) {
        _phoneNumber.value = phoneNumber
    }

    fun setBirthday(birthday: String) {
        _birthday.value = birthday
    }

    fun checkEmailValidity(email: String) {
        val pattern = Patterns.EMAIL_ADDRESS
        val isValid = if (email.isEmpty()) {
            // Поле электронной почты пустое
            false
        } else {
            pattern.matcher(email).matches()
        }
        _isValidEmail.value = isValid
    }
}