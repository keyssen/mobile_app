package com.nodj.hardwareStore.api.model

import kotlinx.serialization.Serializable

@Serializable
data class ReportRemote(
    val totalSum: Double = 0.0,
    val totalCount: Int = 0,
    val listProductFromReport: List<ProducrFromReport> = listOf()
)