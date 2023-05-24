package com.qw73.hildegard.data.otp

interface OtpReceivedInterface {
    fun onOtpReceived(otp: String)
    fun onOtpTimeout()
}