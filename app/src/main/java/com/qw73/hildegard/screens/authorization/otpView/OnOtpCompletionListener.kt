package com.qw73.hildegard.screens.authorization.otpView

interface OnOtpCompletionListener {
    fun onOtpCompleted(otp: String)
    fun onOtpIncomplete()
}