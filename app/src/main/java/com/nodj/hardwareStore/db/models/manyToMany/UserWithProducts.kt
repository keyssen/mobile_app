package com.nodj.hardwareStore.db.models.manyToMany

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.Objects

@Entity(
    tableName = "user_with_products",
    primaryKeys = ["user_id", "product_id"],
)
class UserWithProducts (
    @ColumnInfo(name = "user_id")
    val userId: Int,
    @ColumnInfo(name = "product_id")
    val productId: Int,
    @ColumnInfo(name = "count")
    val count: Int = 0
) {

    override fun hashCode(): Int {
        return Objects.hash(userId, productId)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserWithProducts

        if (userId != other.userId) return false
        if (productId != other.productId) return false
        if (count != other.count) return false

        return true
    }
}
