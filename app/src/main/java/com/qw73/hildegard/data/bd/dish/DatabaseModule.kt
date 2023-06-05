package com.qw73.hildegard.data.bd.dish

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "my-database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideDishDao(database: AppDatabase): DishDao {
        return database.dishDao()
    }
}