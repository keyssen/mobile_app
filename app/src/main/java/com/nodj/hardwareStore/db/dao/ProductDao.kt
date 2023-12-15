package com.nodj.hardwareStore.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nodj.hardwareStore.db.models.Product
import com.nodj.hardwareStore.db.models.helperModels.AdvancedProduct


@Dao
interface ProductDao {
    @Query(
        "SELECT p.*, up.count FROM products AS p " +
                "LEFT JOIN user_with_products AS up " +
                "ON p.product_id = up.product_id"
    )
    fun loadAllProductPaged(): PagingSource<Int, AdvancedProduct>

    @Query(
        "SELECT p.*, up.count FROM products AS p " +
                "LEFT JOIN user_with_products AS up " +
                "ON p.product_id = up.product_id " +
                "WHERE p.name LIKE :name"
    )
    fun loadAllProductPaged(name: String): PagingSource<Int, AdvancedProduct>

    @Query("select * from products where product_id = :id")
    suspend fun getProduct(id: Int): Product

    @Query(
        "SELECT p.*, uwp.count FROM products AS p " +
                "JOIN user_with_products AS uwp ON p.product_id = uwp.product_id " +
                "WHERE uwp.user_id = :userId"
    )
    suspend fun getAllByUser(userId: Int): List<AdvancedProduct>

    @Insert
    suspend fun insert(vararg product: Product)

    @Update
    suspend fun update(product: Product)

    @Delete
    suspend fun delete(product: Product)

    @Query("DELETE FROM products")
    suspend fun deleteAll()
}