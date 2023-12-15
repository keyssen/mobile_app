package com.nodj.hardwareStore.db.repository

import com.nodj.hardwareStore.db.dao.OrderWithProductsDao
import com.nodj.hardwareStore.db.models.manyToMany.OrderWithProducts
import com.nodj.hardwareStore.db.repository.repositoryInterface.OrderWithProductsRepository

class OfflineOrderWithProductsRepository(private val orderWithProductsDao: OrderWithProductsDao) :
    OrderWithProductsRepository {
    override suspend fun insert(orderWithProducts: OrderWithProducts) =
        orderWithProductsDao.insert(orderWithProducts)

    override suspend fun insertMany(ordersWithProducts: List<OrderWithProducts>) {
        orderWithProductsDao.insert(*ordersWithProducts.toTypedArray())
    }

    override suspend fun delete(orderWithProducts: OrderWithProducts) {
        orderWithProductsDao.delete(orderWithProducts)
    }

    override suspend fun deleteAll() {
        orderWithProductsDao.deleteAll()
    }
}