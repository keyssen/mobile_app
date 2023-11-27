package com.nodj.hardwareStore.db.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.nodj.hardwareStore.common.AppContainer
import com.nodj.hardwareStore.db.dao.ProductDao
import com.nodj.hardwareStore.db.models.Product
import com.nodj.hardwareStore.db.models.helperModels.AdvancedProduct
import com.nodj.hardwareStore.db.repository.repositoryInterface.IncompleteProductRepository
import kotlinx.coroutines.flow.Flow

class IncompleteOfflineProductRepository(private val productDao: ProductDao) :
    IncompleteProductRepository {
    override suspend fun getProduct(id: Int): Product = productDao.getProduct(id)
    override suspend fun getByCategory(categoryId: Int): List<Product> =
        productDao.getByCategory(categoryId)

    override fun getByUser(userId: Int): Flow<PagingData<AdvancedProduct>> = Pager(
        config = PagingConfig(
            pageSize = AppContainer.LIMIT,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            productDao.getByUser(userId)
        }
    ).flow

    override fun getAllByUserFlow(userId: Int): Flow<List<AdvancedProduct>> =
        productDao.getAllByUserFlow(userId)

    fun getAllProductsByUserPagingSource(userId: Int): PagingSource<Int, AdvancedProduct> =
        productDao.getByUser(userId)

    override suspend fun getAllByUser(userId: Int): List<AdvancedProduct> =
        productDao.getAllByUser(userId)

    override suspend fun getAllByUserProduct(userId: Int): List<Product> =
        productDao.getAllByUserProduct(userId)

    override fun getAllByUserProductFlow(userId: Int): Flow<List<Product>> =
        productDao.getAllByUserProductFlow(userId)

    override fun loadAllProductPaged(): Flow<PagingData<Product>> = Pager(
        config = PagingConfig(
            pageSize = AppContainer.LIMIT,
            enablePlaceholders = false
        ),
        pagingSourceFactory = productDao::loadAllProductPaged
    ).flow

    fun getAllProductsPagingSource(): PagingSource<Int, Product> = productDao.loadAllProductPaged()

    override suspend fun insert(product: Product) = productDao.insert(product)

    suspend fun insertProducts(products: List<Product>) =
        productDao.insert(*products.toTypedArray())

    override suspend fun update(product: Product) = productDao.update(product)

    override suspend fun delete(product: Product) = productDao.delete(product)

    suspend fun deleteAll() = productDao.deleteAll()
}