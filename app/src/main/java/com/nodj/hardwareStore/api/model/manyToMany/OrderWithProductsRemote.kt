package com.nodj.hardwareStore.api.model.manyToMany

import com.nodj.hardwareStore.db.models.manyToMany.OrderWithProducts
import kotlinx.serialization.Serializable

@Serializable
data class OrderWithProductsRemote(
    val id: Int = 0,
    val orderId: Long,
    val productId: Long,
    val count: Int,
    val currentPrice: Double
)

fun OrderWithProductsRemote.toOrderWithProducts(): OrderWithProducts = OrderWithProducts(
    orderId,
    productId,
    count,
    currentPrice
)

fun OrderWithProducts.toOrderRemote(id: Int): OrderWithProductsRemote = OrderWithProductsRemote(
    id = id,
    orderId,
    productId,
    count,
    currentPrice
)

fun OrderWithProducts.toOrderRemoteForInsert(): OrderWithProductsRemote =
    OrderWithProductsRemote(
        id = 0,
        orderId,
        productId,
        count,
        currentPrice
    )
