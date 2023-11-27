package com.nodj.hardwareStore.db.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.nodj.hardwareStore.common.AppContainer
import com.nodj.hardwareStore.db.dao.CategoryDao
import com.nodj.hardwareStore.db.models.Category
import com.nodj.hardwareStore.db.repository.repositoryInterface.CategoryRepository
import kotlinx.coroutines.flow.Flow

class OfflineCategoryRepository(private val categoryDao: CategoryDao) : CategoryRepository {
    override fun getAllCategories(): Flow<PagingData<Category>> = Pager(
        config = PagingConfig(
            pageSize = AppContainer.LIMIT,
            enablePlaceholders = false
        ),
        pagingSourceFactory = categoryDao::getAllCategories
    ).flow

    fun getAllCategoriesPagingSource(): PagingSource<Int, Category> = categoryDao.getAllCategories()

    override suspend fun getAll(): List<Category> = categoryDao.getAll()

    override suspend fun getByid(id: Int): Category = categoryDao.getByid(id)

    override suspend fun insert(category: Category) = categoryDao.insert(category)

    suspend fun insertCategories(categories: List<Category>) =
        categoryDao.insert(*categories.toTypedArray())

    override suspend fun update(category: Category) = categoryDao.update(category)

    override suspend fun delete(category: Category) = categoryDao.delete(category)

    suspend fun deleteAll() = categoryDao.deleteAll()
}