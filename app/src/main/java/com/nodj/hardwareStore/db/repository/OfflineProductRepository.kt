package com.nodj.hardwareStore.db.repository

import androidx.paging.PagingSource
import com.nodj.hardwareStore.db.dao.ProductDao
import com.nodj.hardwareStore.db.models.Product
import com.nodj.hardwareStore.db.models.helperModels.AdvancedProduct
import com.nodj.hardwareStore.db.repository.repositoryInterface.ProductRepository
import kotlinx.coroutines.flow.Flow

class OfflineProductRepository(private val productDao: ProductDao) : ProductRepository {
    override fun getAll(): Flow<List<Product>> = productDao.getAll()

    override fun getProduct(id: Int): Flow<Product> = productDao.getProduct(id)
    override fun loadAllProductPaged(): PagingSource<Int, Product> = productDao.loadAllProductPaged()

    override suspend fun get(id: Int, userId: Int): AdvancedProduct = productDao.get(id, userId)

    override fun getAllByUserFlow(userId: Int): Flow<List<AdvancedProduct>> = productDao.getAllByUserFlow(userId)

    override suspend fun getAllByUser(userId: Int): List<AdvancedProduct> = productDao.getAllByUser(userId)

    override fun getAllByUserProduct(userId: Int): Flow<List<Product>> = productDao.getAllByUserProduct(userId)

    override suspend fun getByCategory(categoryId: Int): List<Product> = productDao.getByCategory(categoryId)

    override suspend fun getAllByOrder(orderId: Int): List<AdvancedProduct> = productDao.getAllByOrder(orderId)

    override suspend fun insert(product: Product) = productDao.insert(product)

    override suspend fun update(product: Product) = productDao.update(product)

    override suspend fun delete(product: Product) = productDao.delete(product)
}