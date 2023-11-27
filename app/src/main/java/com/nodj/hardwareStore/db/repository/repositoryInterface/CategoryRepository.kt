package com.nodj.hardwareStore.db.repository.repositoryInterface

import androidx.paging.PagingData
import androidx.room.Dao
import com.nodj.hardwareStore.db.models.Category
import kotlinx.coroutines.flow.Flow


@Dao
interface CategoryRepository {
    fun getAllCategories(): Flow<PagingData<Category>>

    suspend fun getAll(): List<Category>

    suspend fun getByid(id: Int): Category

    suspend fun insert(category: Category)

    suspend fun update(category: Category)

    suspend fun delete(category: Category)
}