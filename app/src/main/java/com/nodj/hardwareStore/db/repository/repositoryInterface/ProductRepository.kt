package com.nodj.hardwareStore.db.repository.repositoryInterface

import androidx.paging.PagingSource
import androidx.room.Dao
import com.nodj.hardwareStore.db.models.Product
import com.nodj.hardwareStore.db.models.helperModels.AdvancedProduct
import kotlinx.coroutines.flow.Flow


@Dao
interface ProductRepository {
    fun getAll(): Flow<List<Product>>

    fun getProduct(id: Int): Flow<Product>

    fun loadAllProductPaged(): PagingSource<Int, Product>

    suspend fun get(id: Int, userId: Int): AdvancedProduct

    fun getAllByUserFlow(userId: Int): Flow<List<AdvancedProduct>>

    suspend fun getAllByUser(userId: Int): List<AdvancedProduct>

    fun getAllByUserProduct(userId: Int): Flow<List<Product>>

    suspend fun getByCategory(categoryId: Int): List<Product>

    suspend fun getAllByOrder(orderId: Int): List<AdvancedProduct>

    suspend fun insert(product: Product)

    suspend fun update(product: Product)

    suspend fun delete(product: Product)
}