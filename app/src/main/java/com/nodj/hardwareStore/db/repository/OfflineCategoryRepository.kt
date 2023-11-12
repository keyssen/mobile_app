package com.nodj.hardwareStore.db.repository

import com.nodj.hardwareStore.db.dao.CategoryDao
import com.nodj.hardwareStore.db.models.Category
import com.nodj.hardwareStore.db.repository.repositoryInterface.CategoryRepository
import kotlinx.coroutines.flow.Flow

class OfflineCategoryRepository(private val categoryDao: CategoryDao) : CategoryRepository {
    override fun getAll(): Flow<List<Category>> = categoryDao.getAll()

    override fun getByid(id: Int): Flow<Category> = categoryDao.getByid(id)

    override suspend fun insert(category: Category) = categoryDao.insert(category)

    override suspend fun update(category: Category) = categoryDao.update(category)

    override suspend fun delete(category: Category) = categoryDao.delete(category)
}