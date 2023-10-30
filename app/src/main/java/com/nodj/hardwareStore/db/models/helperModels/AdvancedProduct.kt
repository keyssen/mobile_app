package com.nodj.hardwareStore.db.models.helperModels

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.nodj.hardwareStore.db.models.Product

data class AdvancedProduct (
    @Embedded
    val product: Product,
    @ColumnInfo(name = "count")
    val count: Int
)