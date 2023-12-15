package com.nodj.hardwareStore.db.repository.repositoryInterface

import androidx.paging.PagingData
import androidx.room.Dao
import com.nodj.hardwareStore.db.models.Product
import com.nodj.hardwareStore.db.models.helperModels.AdvancedProduct
import kotlinx.coroutines.flow.Flow


@Dao
interface IncompleteProductRepository {

    suspend fun getProduct(id: Int): Product

    suspend fun getByCategory(categoryId: Int): List<Product>

    suspend fun getAllByUserProduct(userId: Int): List<Product>

    fun getAllByUserProductFlow(userId: Int): Flow<List<Product>>

    fun getAllByUserFlow(userId: Int): Flow<List<AdvancedProduct>>

    fun getByUser(userId: Int): Flow<PagingData<AdvancedProduct>>

    suspend fun getAllByUser(userId: Int): List<AdvancedProduct>

    fun getAll(name: String): Flow<PagingData<Product>>

    suspend fun insert(product: Product)

    suspend fun update(product: Product)

    suspend fun delete(product: Product)
}