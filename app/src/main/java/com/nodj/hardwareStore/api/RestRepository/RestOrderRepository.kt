package com.nodj.hardwareStore.api.RestRepository

import android.util.Log
import com.nodj.hardwareStore.api.MyServerService
import com.nodj.hardwareStore.api.model.helperModel.toAdvancedProduct
import com.nodj.hardwareStore.api.model.helperModel.toProductFromOrder
import com.nodj.hardwareStore.api.model.manyToMany.toOrderRemoteForInsert
import com.nodj.hardwareStore.api.model.toOrder
import com.nodj.hardwareStore.api.model.toOrderRemote
import com.nodj.hardwareStore.db.models.Order
import com.nodj.hardwareStore.db.models.helperModels.ProductFromOrder
import com.nodj.hardwareStore.db.models.manyToMany.OrderWithProducts
import com.nodj.hardwareStore.db.repository.OfflineOrderRepository
import com.nodj.hardwareStore.db.repository.OfflineOrderWithProductsRepository
import com.nodj.hardwareStore.db.repository.OfflineProductRepository
import com.nodj.hardwareStore.db.repository.OfflineUserWithProductsRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.OrderRepository

class RestOrderRepository(
    private val service: MyServerService,
    private val dbOrderRepository: OfflineOrderRepository,
    private val dbOrderWithProductsRepository: OfflineOrderWithProductsRepository,
    private val dbUserWithProductsRepository: OfflineUserWithProductsRepository,
    private val dbProductRepository: OfflineProductRepository,
) : OrderRepository {
    override suspend fun getByOrder(orderId: Int): List<ProductFromOrder> =
        service.getProductFromOrdersByOrder(orderId, "product").map { it.toProductFromOrder() }

    override suspend fun getAllByUser(userId: Int): Map<Order, List<ProductFromOrder>> {
        var orders = service.getOrdersByUser(userId).map { it.toOrder() }
        dbOrderRepository.deleteAll()
        dbOrderWithProductsRepository.deleteAll()
        Log.d("deleteAll", "deleteAll")
        dbOrderRepository.insertOrders(orders)
        Log.d("insertOrders", "insertOrders")
        orders.forEach { order ->
            val orderWidthProducts = getByOrder(order.id).map {
                Log.d("orderId productId", "${order.id} ${it.product.id}")
                OrderWithProducts(
                    orderId = order.id.toLong(),
                    productId = it.product.id.toLong(),
                    count = it.count,
                    currentPrice = it.currentPrice
                )

            }
            dbOrderWithProductsRepository.insertMany(orderWidthProducts)
        }
        return service.getOrdersByUser(userId).map { it.toOrder() to getByOrder(it.id) }.toMap()
    }

    override suspend fun insert(order: Order) {
        val productsByUser = service.getAllByUserAdvancedProducts(order.userId, "product")
            .map { it.toAdvancedProduct() }
        if (!productsByUser.isEmpty()) {
            val currentOrder =
                dbOrderRepository.insertLong(service.createOrder(order.toOrderRemote()).toOrder())
            if (currentOrder.toInt() != 0) {
                productsByUser.forEach() {
                    service.createOrderWithProduct(
                        OrderWithProducts(
                            orderId = currentOrder,
                            productId = it.product.id.toLong(),
                            count = it.count,
                            currentPrice = it.product.price
                        ).toOrderRemoteForInsert()
                    )
                }
                service.getByUserUserWithProducts(order.userId).forEach {
                    service.deleteUserWithProduct(it.id)
                }
            }
        }
    }


    override suspend fun insertOrders(orders: List<Order>) {
    }

    override suspend fun deleteAll() {
    }

}