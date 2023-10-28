package com.nodj.hardwareStore.ui.theme.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nodj.hardwareStore.ui.theme.models.Product
import com.nodj.hardwareStore.ui.theme.models.helperModels.AdvancedProduct


@Dao
interface ProductDao {
    @Query("select * from products order by name collate nocase asc")
    suspend fun getAll(): List<Product>

    @Query("select * from products where product_id = :id")
    suspend fun getByid(id: Int): Product

    @Query(
        "SELECT p.*, uwp.count FROM products AS p " +
                "LEFT JOIN user_with_products AS uwp " +
                "ON uwp.product_id = p.product_id AND uwp.user_id = :userId " +
                "WHERE p.product_id = :id LIMIT 1"
    )
    suspend fun get(id: Int, userId: Int): AdvancedProduct
    @Query(
        "SELECT p.*, uwp.count FROM products AS p " +
                "JOIN user_with_products AS uwp ON p.product_id = uwp.product_id " +
                "WHERE uwp.user_id = :userid"
    )
    suspend fun getAllByUser(userid: Int): List<AdvancedProduct>

    @Query(
        "SELECT p.*, owp.count FROM products AS p " +
                "JOIN order_with_products AS owp ON p.product_id = owp.product_id " +
                "WHERE owp.order_id = :orderId"
    )
    suspend fun getAllByOrder(orderId: Int): List<AdvancedProduct>

    @Insert
    suspend fun insert(product: Product)

    @Update
    suspend fun update(product: Product)

    @Delete
    suspend fun delete(product: Product)
}