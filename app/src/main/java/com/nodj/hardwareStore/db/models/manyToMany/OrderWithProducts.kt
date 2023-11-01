package com.nodj.hardwareStore.db.models.manyToMany

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.Objects

@Entity(
    tableName = "order_with_products",
    primaryKeys = ["order_id", "product_id"],
)
class OrderWithProducts (
    @ColumnInfo(name = "order_id")
    val orderId: Long,
    @ColumnInfo(name = "product_id")
    val productId: Long,
    @ColumnInfo(name = "count")
    val count: Int,
    @ColumnInfo(name = "current_price")
    val currentPrice: Double
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (javaClass != other?.javaClass) {
            return false
        }
        other as OrderWithProducts
        if (orderId == other.orderId && productId == other.productId) {
            return true
        }
        return false
    }

    override fun hashCode(): Int {
        return Objects.hash(orderId, productId)
    }
}
