package com.qw73.hildegard.dependencies


import com.qw73.hildegard.navigate.Navigator
import com.qw73.hildegard.navigate.NavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class NavigatorModule {

    @Binds
    abstract fun bindNavigatorImpl(navigatorImpl: NavigatorImpl) : Navigator
}