package com.nodj.hardwareStore.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nodj.hardwareStore.db.models.Order
import com.nodj.hardwareStore.db.models.helperModels.ProductFromOrder
import kotlinx.coroutines.flow.Flow


@Dao
interface OrderDao {
    @Query("select * from orders order by date collate nocase desc")
    suspend fun  getAll(): List<Order>

    @Query("SELECT o.*, p.*, owp.count, owp.current_price " +
                "FROM orders AS o " +
                "JOIN order_with_products AS owp ON owp.order_id = o.order_id " +
                "JOIN products AS p ON p.product_id = owp.product_id " +
                "WHERE o.user_id = :userId")
    fun getAllByUser(userId: Int): Flow<Map<Order, List<ProductFromOrder>>>

    @Query("SELECT p.*, owp.count, owp.current_price " +
            "FROM orders AS o " +
            "JOIN order_with_products AS owp ON owp.order_id = o.order_id " +
            "JOIN products AS p ON p.product_id = owp.product_id " +
            "WHERE o.order_id = :orderId")
    fun getByOrder(orderId: Int): Flow<List<ProductFromOrder>>

    @Query("select * from orders where user_id = :userId")
    suspend fun getByUserid(userId: Int): List<Order>

    @Insert
    suspend fun insert(order: Order): Long

    @Update
    suspend fun update(order: Order)

    @Delete
    suspend fun delete(order: Order)
}