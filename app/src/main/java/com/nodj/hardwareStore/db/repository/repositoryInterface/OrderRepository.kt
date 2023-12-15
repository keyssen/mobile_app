package com.nodj.hardwareStore.db.repository.repositoryInterface

import androidx.room.Dao
import com.nodj.hardwareStore.db.models.Order
import com.nodj.hardwareStore.db.models.helperModels.ProductFromOrder


@Dao
interface OrderRepository {

    suspend fun getAll(): List<Order>

    suspend fun getAllByUser(userId: Int): Map<Order, List<ProductFromOrder>>
    suspend fun getByOrder(orderId: Int): List<ProductFromOrder>

    suspend fun getByUserid(userId: Int): List<Order>

    suspend fun insert(order: Order): Long

    suspend fun insertOrders(vararg order: Order)

    suspend fun update(order: Order)

    suspend fun delete(order: Order)

    suspend fun deleteAll()
}