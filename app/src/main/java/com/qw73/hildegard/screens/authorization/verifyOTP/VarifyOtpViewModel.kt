package com.qw73.hildegard.screens.authorization.verifyOTP

import android.os.CountDownTimer
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.qw73.hildegard.R
import com.qw73.hildegard.data.EVENT_CHANGE_NUMBER
import com.qw73.hildegard.data.EVENT_RESEND_OTP
import com.qw73.hildegard.data.EVENT_VALIDATE_OTP
import com.qw73.hildegard.data.impl.resrc.IRes
import com.qw73.hildegard.screens.authorization.otpView.OnOtpCompletionListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VarifyOtpViewModel @Inject constructor(val resource: IRes): ViewModel() {

    /* binding variables */
    val loaderVisibility = ObservableBoolean(false)
    val isResendClickable = ObservableBoolean(false)
    val isError = ObservableBoolean(false)
    val isResendLoaderVisible = ObservableBoolean(false)
    val resendText = ObservableField<String>()

    /* data variables */
    var otpArray: CharArray = CharArray(6)

    val validationEvents: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    var isTimerRunning = false
    var isOtpProvided = false

    private val _otp = MutableLiveData<String>("")

    fun onValidateOTPClick() {
        isError.set(!isOtpProvided)
        if (isOtpProvided) {
            loaderVisibility.set(true)
            validationEvents.value = EVENT_VALIDATE_OTP
        }
    }

    fun onChangeNumberClick() {
        validationEvents.value = EVENT_CHANGE_NUMBER
    }

    fun onResendClick() {
        resendText.set("")
        isResendLoaderVisible.set(true)
        validationEvents.value = EVENT_RESEND_OTP
    }

    fun otpSendStatus(isSent: Boolean) {
        isResendLoaderVisible.set(false)
        if (isSent) {
            isTimerRunning = true
            startTimer()
        } else {
            resendText.set(resource.str(R.string.resend))
            isResendClickable.set(true)
        }
    }

    val otpListener = object : OnOtpCompletionListener {
        override fun onOtpCompleted(otp: String) {
            isOtpProvided = true
            otpArray = otp.toCharArray()
            onValidateOTPClick()
        }

        override fun onOtpIncomplete() {
            isOtpProvided = false
            isError.set(false)
        }
    }

    fun startTimer() {
        isResendClickable.set(false)
        if (isTimerRunning) timer.cancel()
        timer.start()
    }


    fun validateOtpState(isSuccessful: Boolean) {
        loaderVisibility.set(false)
        if (isSuccessful) {
            if (isTimerRunning) timer.cancel()
            isTimerRunning = false
        } else isError.set(true)
    }

    fun otp() = otpArray.concatToString()

    private val timer = object : CountDownTimer(30000,1000) {
        override fun onTick(millisUntilFinished: Long) {
            resendText.set(String.format("00:%d", millisUntilFinished / 1000))
        }

        override fun onFinish() {
            resendText.set(resource.str(R.string.resend))
            isResendClickable.set(true)
            isTimerRunning = false
        }

    }
}