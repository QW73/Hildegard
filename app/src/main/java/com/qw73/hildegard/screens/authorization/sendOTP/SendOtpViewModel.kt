package com.qw73.hildegard.screens.authorization.sendOTP

import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.qw73.hildegard.data.EVENT_SEND_OTP
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SendOtpViewModel @Inject constructor(
    private val otpUseCase: OtpUseCase,
) : ViewModel() {

    /* observable fields */
    val PhoneEnabled = ObservableBoolean(true)
    val Loading = ObservableBoolean(false)
    val Error = ObservableBoolean(false)

    /* variables required for otp sending process */
    var mPhoneNumber: String = ""

    /* live data for sending events back to view */
    val otpEvents: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    fun onSendOtpClick() {
        Error.set(false)
        mPhoneNumber =
            mPhoneNumber.replace("+", "").replace("(", "").replace(")", "").replace("-", "")
                .replace(" ", "").replaceFirst("7", "")
        otpUseCase.setPhoneNumber(mPhoneNumber)
        val isNumberValid = otpUseCase.isNumberValid()
        Error.set(!isNumberValid)
        PhoneEnabled.set(!isNumberValid)
        Loading.set(isNumberValid)
        if (isNumberValid) otpEvents.value = EVENT_SEND_OTP
    }

    fun otpUpdateStatus() {
        Loading.set(false)
        PhoneEnabled.set(true)
    }

    fun phoneNumber(): String {
        return otpUseCase.formattedNumber()
    }

    val phoneListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            s?.let {
                mPhoneNumber = it.toString()
                Error.set(false)
            }
        }
    }
}