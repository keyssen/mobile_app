package com.nodj.hardwareStore.api.model

import com.nodj.hardwareStore.db.models.Product
import kotlinx.serialization.Serializable

@Serializable
data class ProductRemote(
    val id: Int = 0,
    val name: String = "",
    val price: Double = 0.0,
    val image: ByteArray = byteArrayOf(),
    val categoryId: Int = 0
)

fun ProductRemote.toProduct(): Product = Product(
    id,
    name,
    price,
    image,
    categoryId
)

fun Product.toProductRemote(): ProductRemote = ProductRemote(
    id,
    name,
    price,
    image,
    categoryId!!
)