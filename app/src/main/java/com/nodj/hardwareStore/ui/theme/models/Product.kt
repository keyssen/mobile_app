package com.nodj.hardwareStore.ui.theme.models

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.nodj.hardwareStore.R

@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["category_id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT
        )
    ]
)
data class Product(
    @ColumnInfo(name = "product_id")
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val name: String,
    val price: Double,
    @DrawableRes
    @ColumnInfo(name = "image_id")
    val imageId: Int,
    @ColumnInfo(name = "category_id", index = true)
    val categoryId: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (javaClass != other?.javaClass) {
            return false
        }
        other as Product
        if (id != other.id) {
            return false
        }
        return true
    }

    override fun hashCode(): Int {
        return id ?: -1
    }

    companion object {
        fun getEmpty(): Product {
            return Product(
                id = null,
                name = "name",
                price = 0.0,
                imageId = R.drawable.i5,
                categoryId = -1
            )
        }
    }
}
