package com.nodj.hardwareStore.api.RestRepository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nodj.hardwareStore.api.MyServerService
import com.nodj.hardwareStore.api.RemoteMediator.ProductRemoteMediator
import com.nodj.hardwareStore.api.model.helperModel.toAdvancedProduct
import com.nodj.hardwareStore.api.model.toProduct
import com.nodj.hardwareStore.api.model.toProductRemote
import com.nodj.hardwareStore.common.AppContainer
import com.nodj.hardwareStore.db.database.AppDatabase
import com.nodj.hardwareStore.db.models.Product
import com.nodj.hardwareStore.db.models.helperModels.AdvancedProduct
import com.nodj.hardwareStore.db.models.manyToMany.UserWithProducts
import com.nodj.hardwareStore.db.remotekeys.dao.OfflineRemoteKeyRepository
import com.nodj.hardwareStore.db.repository.OfflineProductRepository
import com.nodj.hardwareStore.db.repository.OfflineUserWithProductsRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.ProductRepository
import kotlinx.coroutines.flow.Flow

class RestProductRepository(
    private val service: MyServerService,
    private val dbProductRepository: OfflineProductRepository,
    private val dbUserWithProductsRepository: OfflineUserWithProductsRepository,
    private val dbRemoteKeyRepository: OfflineRemoteKeyRepository,
    private val database: AppDatabase
) : ProductRepository {

    override fun getAll(name: String): Flow<PagingData<AdvancedProduct>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = AppContainer.LIMIT,
                enablePlaceholders = false
            ),
            remoteMediator = ProductRemoteMediator(
                service = service,
                dbProductRepository = dbProductRepository,
                dbRemoteKeyRepository = dbRemoteKeyRepository,
                database = database,
            ),
        ) {
            if (name.isEmpty()) {
                dbProductRepository.loadAllProductPaged()
            } else {
                dbProductRepository.loadAllProductPaged(name)
            }
        }.flow
    }

    override suspend fun getProduct(id: Int): Product = service.getProduct(id).toProduct()

    override suspend fun getAllByUserProduct(userId: Int): List<Product> {
        return service.getByUserUserWithProducts(userId)
            .map { service.getProduct(it.productId).toProduct() }
    }


    override suspend fun getAllByUser(userId: Int): List<AdvancedProduct> {
        dbUserWithProductsRepository.deleteAll()
        val listAdvancedProduct: List<AdvancedProduct> =
            service.getAllByUserAdvancedProducts(userId, "product")
                .map { it.toAdvancedProduct() }
        Log.d("size", "${listAdvancedProduct.size} ")
        listAdvancedProduct.forEach {
            Log.d("getAllByUser", "${it.product.id} ${userId} ")
            dbUserWithProductsRepository.insert(
                UserWithProducts(
                    userId = userId,
                    productId = it.product.id,
                    count = it.count
                )
            )
        }
        return listAdvancedProduct
    }


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