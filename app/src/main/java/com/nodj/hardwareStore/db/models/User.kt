package com.nodj.hardwareStore.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @ColumnInfo(name = "user_id")
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    var name: String,
    val role: UserRole,
    val password: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (javaClass != other?.javaClass) {
            return false
        }
        other as User
        if (id != other.id) return false
        if (name != other.name) return false
        if (role != other.role) return false
        if (password != other.password) return false
        return true
    }

    override fun hashCode(): Int {
        return id ?: -1
    }
}
