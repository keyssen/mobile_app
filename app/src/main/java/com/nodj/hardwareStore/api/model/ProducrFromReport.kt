package com.nodj.hardwareStore.api.model

import kotlinx.serialization.Serializable

@Serializable
data class ProducrFromReport(
    val name: String = "",
    val currentPrice: Double = 0.0,
    val sum: Double = 0.0,
    val count: Int = 0,
)