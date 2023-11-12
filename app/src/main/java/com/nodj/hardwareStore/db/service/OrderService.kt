package com.nodj.hardwareStore.db.service

import com.nodj.hardwareStore.db.models.Order
import com.nodj.hardwareStore.db.models.manyToMany.OrderWithProducts
import com.nodj.hardwareStore.db.repository.repositoryInterface.OrderRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.OrderWithProductsRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.ProductRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.UserWithProductsRepository
import kotlinx.coroutines.flow.forEach
import java.util.Date

class OrderService(
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
    private val orderWithProductsRepository: OrderWithProductsRepository,
    private val userWithProductsRepository: UserWithProductsRepository
    ) {


    suspend fun createOrder(userId: Int) {
        var order = orderRepository.insert(Order( date = Date(), userId = userId))
        if (order.toInt() != 0) {
            val productInCartList = productRepository.getAllByUserFlow(userId)
            productInCartList.collect() {
                it.forEach() {
                    orderWithProductsRepository.insert(
                        OrderWithProducts(
                            orderId = order,
                            productId = it.product.id.toLong(),
                            count = it.count,
                            currentPrice = it.product.price
                        )
                    )
                }
                userWithProductsRepository.deleteAllByUser(userId)
            }
        }
    }
}