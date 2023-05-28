package com.qw73.hildegard.data.bd

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.room.*
import java.io.ByteArrayOutputStream

@Entity(tableName = "dishes")
data class Dish(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: String,
    val name: String,
    val image: Uri,
    val price: Int,
    val grams: Int,
    val calories: Int,
    val proteins: Int,
    val fats: Int,
    val carbohydrates: Int,
    val exclusions: List<String>,
    val ingredients: List<String>
)

class ListConverter {
    @TypeConverter
    fun fromList(list: List<String>?): String? {
        return list?.joinToString(",")
    }

    @TypeConverter
    fun toList(string: String?): List<String>? {
        return string?.split(",")?.map { it.trim() }
    }
}

class UriConverter {
    @TypeConverter
    fun fromUri(uri: Uri?): String? {
        return uri?.toString()
    }

    @TypeConverter
    fun toUri(string: String?): Uri? {
        return string?.let { Uri.parse(it) }
    }
}