package com.nodj.hardwareStore.db.repository

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.nodj.hardwareStore.db.dao.ProductDao
import com.nodj.hardwareStore.db.models.Product
import com.nodj.hardwareStore.db.models.helperModels.AdvancedProduct
import com.nodj.hardwareStore.db.repository.repositoryInterface.ProductRepository
import kotlinx.coroutines.flow.Flow

class OfflineProductRepository(private val productDao: ProductDao) :
    ProductRepository {
    override suspend fun getProduct(id: Int): Product = productDao.getProduct(id)
    override suspend fun getAllByUserProduct(userId: Int): List<Product> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllByUser(userId: Int): List<AdvancedProduct> =
        productDao.getAllByUser(userId)

    override fun getAll(name: String): Flow<PagingData<AdvancedProduct>> {
        TODO("Not yet implemented")
    }

    fun loadAllProductPaged(): PagingSource<Int, AdvancedProduct> = productDao.loadAllProductPaged()

    fun loadAllProductPaged(name: String): PagingSource<Int, AdvancedProduct> =
        productDao.loadAllProductPaged(name)

    override suspend fun insert(product: Product) = productDao.insert(product)

    suspend fun insertProducts(products: List<Product>) =
        productDao.insert(*products.toTypedArray())

    override suspend fun update(product: Product) = productDao.update(product)

    override suspend fun delete(product: Product) = productDao.delete(product)

    suspend fun deleteAll() = productDao.deleteAll()
}