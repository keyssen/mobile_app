package com.nodj.hardwareStore.api.RestRepository

import com.nodj.hardwareStore.api.MyServerService
import com.nodj.hardwareStore.api.model.helperModel.toProductFromOrder
import com.nodj.hardwareStore.api.model.toOrder
import com.nodj.hardwareStore.api.model.toOrderRemote
import com.nodj.hardwareStore.db.models.Order
import com.nodj.hardwareStore.db.models.helperModels.ProductFromOrder
import com.nodj.hardwareStore.db.repository.OfflineOrderRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.OrderRepository

class RestOrderRepository(
    private val service: MyServerService,
    private val dbOrderRepository: OfflineOrderRepository,
) : OrderRepository {
    override suspend fun getAll(): List<Order> {
        TODO("Not yet implemented")
    }

    override suspend fun getByOrder(orderId: Int): List<ProductFromOrder> =
        service.getProductFromOrdersByOrder(orderId, "product").map { it.toProductFromOrder() }

    override suspend fun getAllByUser(userId: Int): Map<Order, List<ProductFromOrder>> =
        service.getOrdersByUser(userId).map { it.toOrder() to getByOrder(it.id) }.toMap()

    override suspend fun getByUserid(userId: Int): List<Order> {
        TODO("Not yet implemented")
    }

    override suspend fun insert(order: Order): Long =
        service.createOrder(order.toOrderRemote()).id.toLong()

    override suspend fun update(order: Order) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(order: Order) {
        TODO("Not yet implemented")
    }
}