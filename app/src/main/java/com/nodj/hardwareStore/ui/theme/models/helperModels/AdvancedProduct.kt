package com.nodj.hardwareStore.ui.theme.models.helperModels

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.nodj.hardwareStore.ui.theme.models.Product

data class AdvancedProduct (
    @Embedded
    val product: Product,
    @ColumnInfo(name = "count")
    val count: Int
)