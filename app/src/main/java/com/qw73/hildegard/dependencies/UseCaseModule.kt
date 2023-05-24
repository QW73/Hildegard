package com.qw73.hildegard.dependencies

import com.qw73.hildegard.screens.authorization.sendOTP.OtpUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideOtpUseCase() : OtpUseCase {
        return OtpUseCase()
    }
}