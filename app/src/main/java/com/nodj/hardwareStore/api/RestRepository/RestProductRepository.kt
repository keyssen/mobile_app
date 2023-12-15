package com.nodj.hardwareStore.api.RestRepository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nodj.hardwareStore.api.MyServerService
import com.nodj.hardwareStore.api.RemoteMediator.AdvancedProductRemoteMediator
import com.nodj.hardwareStore.api.RemoteMediator.ProductRemoteMediator
import com.nodj.hardwareStore.api.model.helperModel.toAdvancedProduct
import com.nodj.hardwareStore.api.model.toProduct
import com.nodj.hardwareStore.api.model.toProductRemote
import com.nodj.hardwareStore.common.AppContainer
import com.nodj.hardwareStore.db.database.AppDatabase
import com.nodj.hardwareStore.db.models.Product
import com.nodj.hardwareStore.db.models.helperModels.AdvancedProduct
import com.nodj.hardwareStore.db.remotekeys.dao.OfflineRemoteKeyRepository
import com.nodj.hardwareStore.db.repository.IncompleteOfflineProductRepository
import com.nodj.hardwareStore.db.repository.OfflineUserWithProductsRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.IncompleteProductRepository
import kotlinx.coroutines.flow.Flow

class RestProductRepository(
    private val service: MyServerService,
    private val dbProductRepository: IncompleteOfflineProductRepository,
    private val dbUserWithProductsRepository: OfflineUserWithProductsRepository,
    private val dbRemoteKeyRepository: OfflineRemoteKeyRepository,
    private val database: AppDatabase
) : IncompleteProductRepository {
    override fun loadAllProductPaged(): Flow<PagingData<Product>> {
        Log.d(RestProductRepository::class.simpleName, "Get products")

        val pagingSourceFactory = { dbProductRepository.getAllProductsPagingSource() }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = AppContainer.LIMIT,
                enablePlaceholders = false
            ),
            remoteMediator = ProductRemoteMediator(
                service,
                dbProductRepository,
                dbUserWithProductsRepository,
                dbRemoteKeyRepository,
                database,
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override suspend fun getProduct(id: Int): Product = service.getProduct(id).toProduct()
    override suspend fun getByCategory(categoryId: Int): List<Product> =
        service.getByCategoryProducts(categoryId).map { it.toProduct() }

    override suspend fun getAllByUserProduct(userId: Int): List<Product> {
        val listProduct = service.getByUserUserWithProducts(userId)
            .map { service.getProduct(it.productId).toProduct() }
        return listProduct
    }

    override fun getAllByUserProductFlow(userId: Int): Flow<List<Product>> {
        return dbProductRepository.getAllByUserProductFlow(userId)
    }


    override fun getByUser(userId: Int): Flow<PagingData<AdvancedProduct>> {
        Log.d(RestProductRepository::class.simpleName, "Get getByUser")

        val pagingSourceFactory = { dbProductRepository.getAllProductsByUserPagingSource(userId) }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = AppContainer.LIMIT,
                enablePlaceholders = false
            ),
            remoteMediator = AdvancedProductRemoteMediator(
                service,
                dbProductRepository,
                dbUserWithProductsRepository,
                dbRemoteKeyRepository,
                database,
                userId
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override fun getAllByUserFlow(userId: Int): Flow<List<AdvancedProduct>> {
        return dbProductRepository.getAllByUserFlow(userId)
    }


    override suspend fun getAllByUser(userId: Int): List<AdvancedProduct> =
        service.getAllByUserAdvancedProducts(userId, "product").map { it.toAdvancedProduct() }

    override suspend fun insert(product: Product) {
        service.createProduct(product.toProductRemote()).toProduct()
    }

    override suspend fun update(product: Product) {
        service.updateProduct(product.id, product.toProductRemote()).toProduct()
    }

    override suspend fun delete(product: Product) {
        service.deleteProduct(product.id).toProduct()
    }
}