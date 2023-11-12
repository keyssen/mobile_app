package com.nodj.hardwareStore.db.repository.repositoryInterface

import androidx.room.Dao
import com.nodj.hardwareStore.db.models.Category
import kotlinx.coroutines.flow.Flow


@Dao
interface CategoryRepository {
    fun getAll(): Flow<List<Category>>

    fun getByid(id: Int): Flow<Category>

    suspend fun insert(category: Category)

    suspend fun update(category: Category)

    suspend fun delete(category: Category)
}