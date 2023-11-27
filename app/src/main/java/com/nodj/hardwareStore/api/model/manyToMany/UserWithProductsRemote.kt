package com.nodj.hardwareStore.api.model.manyToMany

import com.nodj.hardwareStore.db.models.manyToMany.UserWithProducts
import kotlinx.serialization.Serializable

@Serializable
data class UserWithProductsRemote(
    val id: Int = 0,
    val userId: Int = 0,
    val productId: Int = 0,
    val count: Int = 0,
)

fun UserWithProductsRemote.toUserWithProducts(): UserWithProducts = UserWithProducts(
    userId,
    productId,
    count,
)

fun UserWithProducts.toUserRemote(id: Int): UserWithProductsRemote = UserWithProductsRemote(
    id = id,
    userId,
    productId,
    count,
)

fun UserWithProducts.toUserRemoteForInsert(): UserWithProductsRemote =
    UserWithProductsRemote(
        id = 0,
        userId,
        productId,
        count,
    )
