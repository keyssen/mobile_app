package com.nodj.hardwareStore.db.models

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.ByteArrayOutputStream

@Entity(
    tableName = "products",
)
data class Product(
    @ColumnInfo(name = "product_id")
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val price: Double,
    @ColumnInfo(name = "image_id", typeAffinity = ColumnInfo.BLOB)
    val image: ByteArray,
    @ColumnInfo(name = "category_id", index = true)
    val categoryId: Int?
) {

    override fun hashCode(): Int {
        return id ?: -1
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product

        if (id != other.id) return false
        if (name != other.name) return false
        if (price != other.price) return false
        if (image != other.image) return false
        if (categoryId != other.categoryId) return false

        return true
    }

    companion object {
        fun getEmpty(): Product {
            return Product(
                id = 0,
                name = "name",
                price = 0.0,
                image = byteArrayOf(),
                categoryId = -1
            )
        }

        fun toBitmap(byteArray: ByteArray): Bitmap {
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        }

        fun fromBitmap(bitmap: Bitmap): ByteArray {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            return outputStream.toByteArray()
        }

        fun contains(listProduct: List<Product>, id: Int): Boolean {
            listProduct.forEach {
                if (it.id == id) return true
            }
            return false
        }
    }
}
