package com.nodj.hardwareStore.db.database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.nodj.hardwareStore.db.models.UserRole
import java.io.ByteArrayOutputStream
import java.util.Date

class Converters {
    @TypeConverter
    fun toUserRole(value: Int): UserRole {
        return enumValues<UserRole>()[value]
    }

    @TypeConverter
    fun fromUserRole(value: UserRole): Int {
        return value.ordinal
    }

    @TypeConverter
    fun toDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}
