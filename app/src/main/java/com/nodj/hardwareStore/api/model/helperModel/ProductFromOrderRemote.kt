package com.nodj.hardwareStore.api.model.helperModel

import com.nodj.hardwareStore.api.model.ProductRemote
import com.nodj.hardwareStore.api.model.toProduct
import com.nodj.hardwareStore.api.model.toProductRemote
import com.nodj.hardwareStore.db.models.helperModels.ProductFromOrder
import kotlinx.serialization.Serializable

@Serializable
data class ProductFromOrderRemote(
    val product: ProductRemote,
    val count: Int,
    val currentPrice: Double
)

fun ProductFromOrderRemote.toProductFromOrder(): ProductFromOrder = ProductFromOrder(
    product.toProduct(),
    count,
    currentPrice
)

fun ProductFromOrder.toProductFromOrderRemote(): ProductFromOrderRemote = ProductFromOrderRemote(
    product.toProductRemote(),
    count,
    currentPrice
)