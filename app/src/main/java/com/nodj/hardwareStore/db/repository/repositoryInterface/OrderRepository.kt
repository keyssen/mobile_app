package com.nodj.hardwareStore.db.repository.repositoryInterface

import androidx.room.Dao
import com.nodj.hardwareStore.db.models.Order
import com.nodj.hardwareStore.db.models.helperModels.ProductFromOrder


@Dao
interface OrderRepository {
    suspend fun getAllByUser(userId: Int): Map<Order, List<ProductFromOrder>>

    suspend fun getByOrder(orderId: Int): List<ProductFromOrder>

    suspend fun insert(order: Order)

    suspend fun insertOrders(orders: List<Order>)

    suspend fun deleteAll()
}