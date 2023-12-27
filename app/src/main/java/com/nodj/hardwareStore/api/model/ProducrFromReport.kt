package com.nodj.hardwareStore.api.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductFromReport(
    val name: String = "",
    val currentPrice: Double = 0.0,
    val sum: Double = 0.0,
    val count: Int = 0,
)