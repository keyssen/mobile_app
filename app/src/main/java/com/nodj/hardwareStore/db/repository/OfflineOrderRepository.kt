package com.nodj.hardwareStore.db.repository

import com.nodj.hardwareStore.db.dao.OrderDao
import com.nodj.hardwareStore.db.models.Order
import com.nodj.hardwareStore.db.models.helperModels.ProductFromOrder
import com.nodj.hardwareStore.db.repository.repositoryInterface.OrderRepository

class OfflineOrderRepository(private val orderDao: OrderDao) : OrderRepository {
    override suspend fun getAll(): List<Order> = orderDao.getAll()

    override suspend fun getAllByUser(userId: Int): Map<Order, List<ProductFromOrder>> =
        orderDao.getAllByUser(userId)

    override suspend fun getByOrder(orderId: Int): List<ProductFromOrder> =
        orderDao.getByOrder(orderId)

    override suspend fun getByUserid(userId: Int): List<Order> = orderDao.getByUserid(userId)

    override suspend fun insert(order: Order): Long = orderDao.insert(order)

    override suspend fun update(order: Order) = orderDao.update(order)

    override suspend fun delete(order: Order) = orderDao.delete(order)
}