package com.qw73.hildegard.data.bd.dish

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.qw73.hildegard.data.bd.сonverters.ListConverter
import com.qw73.hildegard.data.bd.сonverters.UriConverter


@TypeConverters(ListConverter::class, UriConverter::class)
@Database(entities = [Dish::class, Order::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dishDao(): DishDao

}