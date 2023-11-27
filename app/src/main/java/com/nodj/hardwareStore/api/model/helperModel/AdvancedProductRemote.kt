package com.nodj.hardwareStore.api.model.helperModel

import com.nodj.hardwareStore.api.model.ProductRemote
import com.nodj.hardwareStore.api.model.toProduct
import com.nodj.hardwareStore.api.model.toProductRemote
import com.nodj.hardwareStore.db.models.helperModels.AdvancedProduct
import kotlinx.serialization.Serializable

@Serializable
data class AdvancedProductRemote(
    val product: ProductRemote,
    val count: Int
)

fun AdvancedProductRemote.toAdvancedProduct(): AdvancedProduct = AdvancedProduct(
    product.toProduct(),
    count
)

fun AdvancedProduct.toAdvancedProductRemote(): AdvancedProductRemote = AdvancedProductRemote(
    product.toProductRemote(),
    count
)