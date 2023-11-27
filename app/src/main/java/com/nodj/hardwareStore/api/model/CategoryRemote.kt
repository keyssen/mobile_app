package com.nodj.hardwareStore.api.model

import com.nodj.hardwareStore.db.models.Category
import kotlinx.serialization.Serializable

@Serializable
data class CategoryRemote(
    val id: Int = 0,
    val name: String = ""
)

fun CategoryRemote.toCategory(): Category = Category(
    id,
    name
)

fun Category.toCategoryRemote(): CategoryRemote = CategoryRemote(
    id,
    name
)