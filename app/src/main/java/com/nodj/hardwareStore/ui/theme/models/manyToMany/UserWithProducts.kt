package com.nodj.hardwareStore.ui.theme.models.manyToMany

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import java.util.Objects

@Entity(
    tableName = "user_with_products",
    primaryKeys = ["user_id", "product_id"],
)
class UserWithProducts (
    @ColumnInfo(name = "user_id")
    val userId: Long,
    @ColumnInfo(name = "product_id")
    val productId: Long,
    @ColumnInfo(name = "count")
    val count: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (javaClass != other?.javaClass) {
            return false
        }
        other as UserWithProducts
        if (userId == other.userId && productId == other.productId) {
            return true
        }
        return false
    }

    override fun hashCode(): Int {
        return Objects.hash(userId, productId)
    }
}
