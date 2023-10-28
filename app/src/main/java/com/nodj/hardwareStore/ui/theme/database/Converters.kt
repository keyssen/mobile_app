package com.nodj.hardwareStore.ui.theme.database

import androidx.room.TypeConverter
import com.nodj.hardwareStore.ui.theme.models.UserRole
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
}
