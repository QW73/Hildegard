package com.qw73.hildegard.data.bd.dish

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.qw73.hildegard.data.bd.сonverter.ListConverter
import com.qw73.hildegard.data.bd.сonverter.UriConverter


@TypeConverters(ListConverter::class, UriConverter::class)
@Database(entities = [Dish::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dishDao(): DishDao

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "dish_database"
                    )
                        .build()
                }
            }
            return instance as AppDatabase
        }
    }
}