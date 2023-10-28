package com.nodj.hardwareStore.ui.theme.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.nodj.hardwareStore.ui.theme.models.manyToMany.OrderWithProducts
import com.nodj.hardwareStore.ui.theme.models.manyToMany.UserWithProducts

@Dao
interface OrderWithProductsDao {
    @Insert
    suspend fun insert(orderWithProducts: OrderWithProducts)

    @Update
    suspend fun update(orderWithProducts: OrderWithProducts)

    @Delete
    suspend fun delete(orderWithProducts: OrderWithProducts)
}