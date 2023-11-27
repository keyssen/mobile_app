package com.nodj.hardwareStore.api.RestRepository

import com.nodj.hardwareStore.api.MyServerService
import com.nodj.hardwareStore.api.model.manyToMany.toOrderRemoteForInsert
import com.nodj.hardwareStore.db.models.manyToMany.OrderWithProducts
import com.nodj.hardwareStore.db.repository.OfflineOrderWithProductsRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.OrderWithProductsRepository

class RestOrderWithProductsRepository(
    private val service: MyServerService,
    private val dbOrderWithProductsRepository: OfflineOrderWithProductsRepository,
) : OrderWithProductsRepository {
    override suspend fun insert(orderWithProducts: OrderWithProducts) {
        val respone = service.createOrderWithProduct(orderWithProducts.toOrderRemoteForInsert())
    }

    override suspend fun update(orderWithProducts: OrderWithProducts) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(orderWithProducts: OrderWithProducts) {
        TODO("Not yet implemented")
    }

}