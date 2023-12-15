package com.nodj.hardwareStore.api.RestRepository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nodj.hardwareStore.api.MyServerService
import com.nodj.hardwareStore.api.RemoteMediator.CategoryRemoteMediator
import com.nodj.hardwareStore.api.model.toCategory
import com.nodj.hardwareStore.api.model.toCategoryRemote
import com.nodj.hardwareStore.common.AppContainer
import com.nodj.hardwareStore.db.database.AppDatabase
import com.nodj.hardwareStore.db.models.Category
import com.nodj.hardwareStore.db.remotekeys.dao.OfflineRemoteKeyRepository
import com.nodj.hardwareStore.db.repository.OfflineCategoryRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.CategoryRepository
import kotlinx.coroutines.flow.Flow

class RestCategoryRepository(
    private val service: MyServerService,
    private val dbCategoryRepository: OfflineCategoryRepository,
    private val dbRemoteKeyRepository: OfflineRemoteKeyRepository,
    private val database: AppDatabase
) : CategoryRepository {
    override suspend fun getAll(): List<Category> {
        Log.d(RestCategoryRepository::class.simpleName, "getAll")
        dbCategoryRepository.deleteAll()
        var serviceCategories = service.getAllСategories().map { it.toCategory() }
        dbCategoryRepository.insertCategories(serviceCategories)
        return serviceCategories
    }

    override fun getAllCategories(): Flow<PagingData<Category>> {
        Log.d(RestCategoryRepository::class.simpleName, "getAllCategories PagingData")

        val pagingSourceFactory = { dbCategoryRepository.getAllCategoriesPagingSource() }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = AppContainer.LIMIT,
                enablePlaceholders = false
            ),
            remoteMediator = CategoryRemoteMediator(
                service,
                dbCategoryRepository,
                dbRemoteKeyRepository,
                database,
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override suspend fun getByid(id: Int): Category = service.getCategory(id).toCategory()

    override suspend fun insert(category: Category) {
        service.createCategory(category.toCategoryRemote()).toCategory()
    }

    override suspend fun update(category: Category) {
        service.updateCategory(category.id, category.toCategoryRemote()).toCategory()
    }

    override suspend fun delete(category: Category) {
        service.deleteCategory(category.id).toCategory()
    }
}