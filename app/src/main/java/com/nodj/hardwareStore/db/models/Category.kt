package com.nodj.hardwareStore.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category (
    @ColumnInfo(name = "category_id")
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String
){
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (javaClass != other?.javaClass) {
            return false
        }
        other as Category
        if (id != other.id) return false
        if (name != other.name) return false
        return true
    }

    companion object {
        fun getEmpty(): Category {
            return Category(
                id = 0,
                name = "",
            )
        }
    }
}
