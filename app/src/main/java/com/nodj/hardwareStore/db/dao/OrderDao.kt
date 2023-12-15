package com.nodj.hardwareStore.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nodj.hardwareStore.db.models.Order
import com.nodj.hardwareStore.db.models.helperModels.ProductFromOrder


@Dao
interface OrderDao {
    @Query("select * from orders order by date collate nocase desc")
    suspend fun getAll(): List<Order>

    @Query(
        "SELECT o.*, p.*, owp.count, owp.current_price " +
                "FROM orders AS o " +
                "JOIN order_with_products AS owp ON owp.order_id = o.order_id " +
                "JOIN products AS p ON p.product_id = owp.product_id " +
                "WHERE o.user_id = :userId"
    )
    suspend fun getAllByUser(userId: Int): Map<Order, List<ProductFromOrder>>

    @Query(
        "SELECT p.*, owp.count, owp.current_price " +
                "FROM orders AS o " +
                "JOIN order_with_products AS owp ON owp.order_id = o.order_id " +
                "JOIN products AS p ON p.product_id = owp.product_id " +
                "WHERE o.order_id = :orderId"
    )
    suspend fun getByOrder(orderId: Int): List<ProductFromOrder>

    @Query("select * from orders where user_id = :userId")
    suspend fun getByUserid(userId: Int): List<Order>

    @Insert
    suspend fun insert(order: Order): Long

    @Insert
    suspend fun insertOrders(vararg order: Order)

    @Update
    suspend fun update(order: Order)

    @Delete
    suspend fun delete(order: Order)

    @Query("DELETE FROM orders")
    suspend fun deleteAll()
}