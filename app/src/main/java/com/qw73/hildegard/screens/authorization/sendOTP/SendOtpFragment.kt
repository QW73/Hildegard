package com.qw73.hildegard.screens.authorization.sendOTP

import android.R
import android.app.Activity
import android.content.IntentSender
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.google.android.gms.auth.api.credentials.Credential
import com.qw73.hildegard.data.DELAY_PHONE_PROMPT
import com.qw73.hildegard.data.getPhoneHintIntent
import com.qw73.hildegard.databinding.FragmentOtpSendBinding
import com.vicmikhailau.maskededittext.MaskedEditText
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SendOtpFragment: Fragment() {

    /* view objects */
    lateinit var editPhone: MaskedEditText
    lateinit var viewBinding: FragmentOtpSendBinding

    /* view mode object */
    private val viewModel: SendOtpViewModel by activityViewModels()

    /* phone number prompt launcher object */
    private lateinit var promptLauncher: ActivityResultLauncher<IntentSenderRequest>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewBinding = FragmentOtpSendBinding.inflate(inflater,container,false)
        viewBinding.viewModel = viewModel
        editPhone = viewBinding.etPhone
        bindViews()
        registerPhonePrompt()
        return viewBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = getPhoneHintIntent(activity as FragmentActivity)
            promptLauncher.launch(IntentSenderRequest.Builder(intent).build())
        }, DELAY_PHONE_PROMPT)
    }


    private fun bindViews() {
        editPhone.addTextChangedListener(viewModel.phoneListener)
    }

    //не пон

    private fun registerPhonePrompt() {
        try {
            promptLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val credential: Credential? = it.data?.getParcelableExtra(Credential.EXTRA_KEY)
                    viewBinding.etPhone.setText(credential?.id)   /* the selected phone number from hint prompt */
                }
            }
        } catch (e: IntentSender.SendIntentException) {
            e.printStackTrace()
        }
    }

    companion object Companion {
        fun instance(): SendOtpFragment {
            return SendOtpFragment()
        }
    }
}