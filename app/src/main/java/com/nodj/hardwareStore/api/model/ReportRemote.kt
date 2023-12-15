package com.nodj.hardwareStore.api.model

import kotlinx.serialization.Serializable

@Serializable
data class ReportRemote(
    val name: String = "",
    val sum: Double = 0.0,
)