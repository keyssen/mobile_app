package com.nodj.hardwareStore.db.repository.repositoryInterface

import androidx.paging.PagingData
import androidx.room.Dao
import com.nodj.hardwareStore.db.models.Product
import com.nodj.hardwareStore.db.models.helperModels.AdvancedProduct
import kotlinx.coroutines.flow.Flow


@Dao
interface ProductRepository {

    suspend fun getProduct(id: Int): Product

    suspend fun getAllByUserProduct(userId: Int): List<Product>

    suspend fun getAllByUser(userId: Int): List<AdvancedProduct>

    fun getAll(name: String): Flow<PagingData<AdvancedProduct>>

    suspend fun insert(product: Product)

    suspend fun update(product: Product)

    suspend fun delete(product: Product)
}