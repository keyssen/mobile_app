package com.nodj.hardwareStore.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nodj.hardwareStore.db.models.Order


@Dao
interface OrderDao {
    @Query("select * from orders order by date collate nocase desc")
    suspend fun  getAll(): List<Order>

    @Query("select * from orders where user_id = :userId")
    suspend fun getByUserid(userId: Int): List<Order>

    @Insert
    suspend fun insert(order: Order)

    @Update
    suspend fun update(order: Order)

    @Delete
    suspend fun delete(order: Order)
}