package com.nodj.hardwareStore.common

import com.nodj.hardwareStore.db.models.Category


interface RemoteCategoryRepository {
    suspend fun getAllCategories(): List<Category>
    suspend fun getCategory(uid: Int): Category
    suspend fun insertCategory(category: Category)
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(category: Category)
}