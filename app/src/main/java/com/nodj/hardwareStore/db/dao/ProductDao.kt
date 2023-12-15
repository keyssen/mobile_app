package com.nodj.hardwareStore.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nodj.hardwareStore.db.models.Product
import com.nodj.hardwareStore.db.models.helperModels.AdvancedProduct
import kotlinx.coroutines.flow.Flow


@Dao
interface ProductDao {

    @Query("select * from products order by name collate nocase asc")
    fun loadAllProductPaged(): PagingSource<Int, Product>

    @Query(
        "select * from products WHERE name LIKE :name order by name collate nocase asc"
    )
    fun loadAllProductPaged(name: String): PagingSource<Int, Product>

    @Query("select * from products where product_id = :id")
    suspend fun getProduct(id: Int): Product

    @Insert
    suspend fun insert(vararg product: Product)

    @Update
    suspend fun update(product: Product)

    @Delete
    suspend fun delete(product: Product)

    @Query("DELETE FROM products")
    suspend fun deleteAll()

    @Query(
        "SELECT p.* FROM products AS p " +
                "JOIN user_with_products AS uwp ON p.product_id = uwp.product_id " +
                "WHERE uwp.user_id = :userId"
    )
    suspend fun getAllByUserProduct(userId: Int): List<Product>

    @Query(
        "SELECT p.* FROM products AS p " +
                "JOIN user_with_products AS uwp ON p.product_id = uwp.product_id " +
                "WHERE uwp.user_id = :userId"
    )
    fun getAllByUserProductFlow(userId: Int): Flow<List<Product>>

    @Query("select * from products where category_id = :categoryId")
    suspend fun getByCategory(categoryId: Int): List<Product>

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
                "WHERE uwp.user_id = :userId"
    )
    suspend fun getAllByUser(userId: Int): List<AdvancedProduct>

    @Query(
        "SELECT p.*, uwp.count FROM products AS p " +
                "JOIN user_with_products AS uwp ON p.product_id = uwp.product_id " +
                "WHERE uwp.user_id = :userId"
    )
    fun getAllByUserFlow(userId: Int): Flow<List<AdvancedProduct>>

    @Query(
        "SELECT p.*, uwp.count FROM products AS p " +
                "JOIN user_with_products AS uwp ON p.product_id = uwp.product_id " +
                "WHERE uwp.user_id = :userId"
    )
    fun getByUser(userId: Int): PagingSource<Int, AdvancedProduct>

    @Query(
        "SELECT p.*, owp.count FROM products AS p " +
                "JOIN order_with_products AS owp ON p.product_id = owp.product_id " +
                "WHERE owp.order_id = :orderId"
    )
    suspend fun getAllByOrder(orderId: Int): List<AdvancedProduct>
}