package com.qw73.hildegard.data.bd.dish

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dishes")
data class Dish(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: String,
    val name: String,
    val image: Uri?,
    val price: Int,
    val grams: Int,
    val calories: Int,
    val proteins: Int,
    val fats: Int,
    val carbohydrates: Int,
    val exclusions: List<String>,
    val ingredients: List<String>,
    val count: Int,
)


@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val phone: String,
    val date: String,
    val time: String,
    val street: String,
    val floor: String,
    val apartment: String,
    val dish: String,
    val dishCount: Int,
    val totalPrice: Int,
)




