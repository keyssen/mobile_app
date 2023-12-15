package com.nodj.hardwareStore.db.repository

import com.nodj.hardwareStore.db.dao.OrderDao
import com.nodj.hardwareStore.db.models.Order
import com.nodj.hardwareStore.db.models.helperModels.ProductFromOrder
import com.nodj.hardwareStore.db.repository.repositoryInterface.OrderRepository

class OfflineOrderRepository(private val orderDao: OrderDao) : OrderRepository {
    override suspend fun getAllByUser(userId: Int): Map<Order, List<ProductFromOrder>> =
        orderDao.getAllByUser(userId)

    override suspend fun getByOrder(orderId: Int): List<ProductFromOrder> =
        orderDao.getByOrder(orderId)

    override suspend fun insert(order: Order) {
        TODO("Not yet implemented")
    }

    suspend fun insertLong(order: Order): Long = orderDao.insert(order)

    override suspend fun insertOrders(orders: List<Order>) =
        orderDao.insertOrders(*orders.toTypedArray())

    override suspend fun deleteAll() {
        orderDao.deleteAll()
    }
}