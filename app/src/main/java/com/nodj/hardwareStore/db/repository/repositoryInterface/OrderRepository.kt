package com.nodj.hardwareStore.db.repository.repositoryInterface

import androidx.room.Dao
import com.nodj.hardwareStore.db.models.Order
import com.nodj.hardwareStore.db.models.helperModels.ProductFromOrder
import kotlinx.coroutines.flow.Flow


@Dao
interface OrderRepository {

    suspend fun  getAll(): List<Order>

    fun getAllByUser(userId: Int): Flow<Map<Order, List<ProductFromOrder>>>

    fun getByOrder(orderId: Int): Flow<List<ProductFromOrder>>

    suspend fun getByUserid(userId: Int): List<Order>

    suspend fun insert(order: Order): Long

    suspend fun update(order: Order)

    suspend fun delete(order: Order)
}