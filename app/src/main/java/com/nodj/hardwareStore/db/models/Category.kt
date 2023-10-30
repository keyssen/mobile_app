package com.nodj.hardwareStore.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category (
    @ColumnInfo(name = "category_id")
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val name: String
){
    companion object {
        fun getEmpty(): Category {
            return Category(
                id = null,
                name = "",
            )
        }
    }
}
