package com.qw73.hildegard.screens.authorization.sendOTP

import android.util.Log
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import javax.inject.Inject


class OtpUseCase @Inject constructor() {

    private val phoneUtil = PhoneNumberUtil.getInstance()
    private var phoneProto: Phonenumber.PhoneNumber? = null

    fun setPhoneNumber(PhoneNumber: String) {
        try {
            phoneProto = phoneUtil.parse(PhoneNumber, "RU")
        } catch (e: NumberParseException) {
            Log.e("exception", e.message + "")
        }
    }

    fun isNumberValid(): Boolean {
        return phoneProto != null && phoneUtil.isValidNumber(phoneProto)
    }

    fun formattedNumber(): String {
        return phoneProto?.let {
            phoneUtil.format(it, PhoneNumberUtil.PhoneNumberFormat.E164)
        }.toString()
    }
}