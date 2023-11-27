package com.nodj.hardwareStore.api.model

import DateSerializer
import com.nodj.hardwareStore.db.models.Order
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class OrderRemote(
    val id: Int = 0,
    @Serializable(with = DateSerializer::class)
    val date: Date = Date(),
    val userId: Int = 0,
)

fun OrderRemote.toOrder(): Order = Order(
    id,
    date,
    userId
)

fun Order.toOrderRemote(): OrderRemote = OrderRemote(
    id,
    date,
    userId
)