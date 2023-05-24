package com.qw73.hildegard.data

import android.app.PendingIntent
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.CredentialsOptions
import com.google.android.gms.auth.api.credentials.HintRequest

    fun getPhoneHintIntent(activity: FragmentActivity): PendingIntent {
        val hintRequest = HintRequest.Builder()
            .setPhoneNumberIdentifierSupported(true)
            .build()

        val options = CredentialsOptions.Builder()
            .forceEnableSaveDialog()
            .build()

        return Credentials.getClient(activity, options).getHintPickerIntent(hintRequest)
    }
