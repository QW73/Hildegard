package com.qw73.hildegard.dependencies

import com.qw73.hildegard.data.impl.prefs.IPref
import com.qw73.hildegard.data.impl.prefs.PrefImpl
import com.qw73.hildegard.data.impl.resrc.IRes
import com.qw73.hildegard.data.impl.resrc.ResImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ResourceWrapperModule {

    @Singleton
    @Binds
    abstract fun bindResImpl(resImpl: ResImpl): IRes

    @Singleton
    @Binds
    abstract fun bindPrefImpl(prefImpl: PrefImpl): IPref
}