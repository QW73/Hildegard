package com.qw73.hildegard.dependencies


import com.qw73.hildegard.data.otp.IOtpManager
import com.qw73.hildegard.data.otp.OtpManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class OtpModule {

    @Binds
    abstract fun bindOtpManagerImpl(otpManagerImpl: OtpManagerImpl): IOtpManager
}