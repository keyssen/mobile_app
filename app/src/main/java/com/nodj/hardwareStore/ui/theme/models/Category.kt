package com.nodj.hardwareStore.ui.theme.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nodj.hardwareStore.R

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
