package com.nodj.hardwareStore.db.repository.repositoryInterface

import androidx.room.Dao
import com.nodj.hardwareStore.db.models.manyToMany.OrderWithProducts

@Dao
interface OrderWithProductsRepository {
    suspend fun insert(orderWithProducts: OrderWithProducts)

    suspend fun insertMany(ordersWithProducts: List<OrderWithProducts>)

    suspend fun delete(orderWithProducts: OrderWithProducts)

    suspend fun deleteAll()
}