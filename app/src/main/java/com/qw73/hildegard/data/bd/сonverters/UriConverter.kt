package com.qw73.hildegard.data.bd.сonverters

import android.net.Uri
import androidx.room.TypeConverter

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