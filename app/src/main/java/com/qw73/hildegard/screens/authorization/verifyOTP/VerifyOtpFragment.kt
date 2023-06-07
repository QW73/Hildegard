package com.qw73.hildegard.screens.authorization.verifyOTP

import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.qw73.hildegard.data.EVENT_RESEND_OTP
import com.qw73.hildegard.data.otp.OtpReceivedInterface
import com.qw73.hildegard.data.otp.SmsBroadcastReceiver
import com.qw73.hildegard.databinding.FragmentOtpVerifyBinding
import com.qw73.hildegard.screens.authorization.AuthorizationActivity
import com.qw73.hildegard.screens.authorization.otpView.OtpView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyOtpFragment : Fragment(), OtpReceivedInterface {

    /* view objects */
    private lateinit var otpView: OtpView
    lateinit var viewBinding: FragmentOtpVerifyBinding

    /* sms retriever to auto read and populate otp */
    private val smsReceiver = SmsBroadcastReceiver()

    /* view model objects */
    private val viewModel: VerifyOtpViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startSmsListener()
        bindSmsReceiver()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewBinding = FragmentOtpVerifyBinding.inflate(inflater, container, false)
        viewBinding.viewModel = viewModel
        bindViews()
        bindObserver()
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.startTimer()
    }

    private fun bindObserver() {
        viewModel.validationEvents.observe(activity as AuthorizationActivity) {
            when (it) {
                EVENT_RESEND_OTP -> otpView.setText("")
            }
        }
    }


    private fun bindSmsReceiver() {
        smsReceiver.otpReceiveInterface = this
        val intentFilter = IntentFilter()
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
        activity?.registerReceiver(smsReceiver, intentFilter)
    }

    private fun startSmsListener() {
        val mClient = SmsRetriever.getClient(activity as FragmentActivity)
        val mTask = mClient.startSmsRetriever()
        mTask.addOnSuccessListener { Log.e("sms listener", "started") }
        mTask.addOnFailureListener { Log.e("sms listener", "failed") }
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.unregisterReceiver(smsReceiver)
    }


    private fun bindViews() {
        otpView = viewBinding.otpView
        otpView.setOtpCompletionListener(viewModel.otpListener)
    }

    companion object Companion {
        fun instance(): VerifyOtpFragment {
            return VerifyOtpFragment()
        }
    }

    override fun onOtpReceived(otp: String) {
        otpView.setText(otp)
        viewModel.onValidateOTPClick()
    }

    override fun onOtpTimeout() {}
}