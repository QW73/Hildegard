package com.qw73.hildegard.screens.authorization

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import com.davidmiguel.numberkeyboard.NumberKeyboard
import com.davidmiguel.numberkeyboard.NumberKeyboardListener
import com.qw73.hildegard.R
import com.qw73.hildegard.base.BaseActivity
import com.qw73.hildegard.data.*
import com.qw73.hildegard.data.impl.prefs.IPref
import com.qw73.hildegard.data.impl.resrc.IRes
import com.qw73.hildegard.data.otp.IOtpManager
import com.qw73.hildegard.databinding.ActivityOtpBinding
import com.qw73.hildegard.navigate.Navigator
import com.qw73.hildegard.screens.authorization.sendOTP.SendOtpFragment
import com.qw73.hildegard.screens.authorization.sendOTP.SendOtpViewModel
import com.qw73.hildegard.screens.authorization.verifyOTP.VarifyOtpViewModel
import com.qw73.hildegard.screens.authorization.verifyOTP.VerifyOtpFragment
import com.qw73.hildegard.screens.main.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthorizationActivity : BaseActivity(), NumberKeyboardListener {
    private lateinit var binding: ActivityOtpBinding

    // private lateinit var bannerHeaderView: TextView
    //  private lateinit var bannerSubHeaderView: TextView
    private lateinit var bannerImage: ImageView

    /* dependency objects */
    @Inject
    lateinit var navigor: Navigator

    @Inject
    lateinit var pref: IPref

    @Inject
    lateinit var res: IRes

    @Inject
    lateinit var otpManager: IOtpManager

    lateinit var numberKeyboard: NumberKeyboard

    lateinit var currentOtp: String

    /* view models objects */
    private val sendOtpViewModel: SendOtpViewModel by viewModels()
    private val verifyOtpViewModel: VarifyOtpViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    /* variable to identifier otp request state */
    private var otpRequestState = EVENT_SEND_OTP

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        otpManager.initFirebase()
        bindViews()
        bindFragment(SendOtpFragment.instance(), SendOtpFragment::class.simpleName)
        bindObservers()
        numberKeyboard = findViewById<NumberKeyboard>(R.id.numberKeyboard)
        numberKeyboard.setListener(this)
    }

    override fun onNumberClicked(number: Int) {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.login_frame)

        if (currentFragment is SendOtpFragment) {
            currentFragment.viewBinding.etPhone.text?.append(number.toString())
            if (currentFragment.viewBinding.etPhone.text?.length == 18) {
                sendOtpViewModel.onSendOtpClick()
            }
        }

        if (currentFragment is VerifyOtpFragment) {
            currentFragment.viewBinding.otpView.append(number.toString())
        }
    }

    override fun onLeftAuxButtonClicked() {

    }

    override fun onRightAuxButtonClicked() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.login_frame)

        if (currentFragment is SendOtpFragment) {
            var currentText = currentFragment.viewBinding.etPhone.text
            if (currentText != null) {
                currentFragment.viewBinding.etPhone.text!!.delete(currentText.length - 1,
                    currentText.length)
            }
        }

        if (currentFragment is VerifyOtpFragment) {
            var currentText = currentFragment.viewBinding.otpView.text
            if (currentText != null) {
                currentFragment.viewBinding.otpView.text!!.delete(currentText.length - 1,
                    currentText.length)
            }
        }
    }

    private fun bindObservers() {
        sendOtpViewModel.otpEvents.observe(this) {
            when (it) {
                EVENT_SEND_OTP -> {
                    otpRequestState = EVENT_SEND_OTP
                    otpManager.sendOtp(sendOtpViewModel.phoneNumber())
                }
            }
        }

        verifyOtpViewModel.validationEvents.observe(this) {
            when (it) {
                EVENT_RESEND_OTP -> {
                    otpRequestState = EVENT_RESEND_OTP
                    otpManager.sendOtp(sendOtpViewModel.phoneNumber(), resend = true)
                }
                EVENT_CHANGE_NUMBER -> supportFragmentManager.popBackStack()
                EVENT_VALIDATE_OTP -> otpManager.verifyOtp(verifyOtpViewModel.otp())
            }
        }

        otpManager.subscribe().observe(this) {
            when (it) {
                EVENT_SEND_SUCCESS -> {
                    showMessage(String.format(getString(R.string.otp_sent),
                        sendOtpViewModel.phoneNumber()))
                    when (otpRequestState) {
                        EVENT_SEND_OTP -> {
                            sendOtpViewModel.otpUpdateStatus()
                            bindFragment(VerifyOtpFragment.instance(),
                                VerifyOtpFragment::class.simpleName)
                        }
                        EVENT_RESEND_OTP -> verifyOtpViewModel.otpSendStatus(true)
                    }
                }
                EVENT_SEND_FAILED -> {
                    when (otpRequestState) {
                        EVENT_SEND_OTP -> sendOtpViewModel.otpUpdateStatus()
                        EVENT_RESEND_OTP -> verifyOtpViewModel.otpSendStatus(false)
                    }
                    showMessage(String.format(getString(R.string.otp_not_sent),
                        sendOtpViewModel.phoneNumber()))
                }
                EVENT_VALIDATE_SUCCESS -> {
                    verifyOtpViewModel.validateOtpState(true)
                    pref.put(KEY_USER_AUTHORIZATION, true)
                    pref.put("phone_number", sendOtpViewModel.phoneNumber())
                    navigor.navigateToMain()
                }
                EVENT_VALIDATE_FAILED -> verifyOtpViewModel.validateOtpState(false)
            }
        }
    }

    private fun bindFragment(fragment: Fragment, tag: String?) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_in, R.anim.fade_out)
            .replace(R.id.login_frame, fragment, tag)
            .addToBackStack(tag)
            .commit()

    }

    private fun bindViews() {
        //
    }

    override fun binding(): ViewBinding {
        return ActivityOtpBinding.inflate(layoutInflater)
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}