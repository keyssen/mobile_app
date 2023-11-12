package com.nodj.hardwareStore.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nodj.hardwareStore.db.models.helperModels.AdvancedProduct
import com.nodj.hardwareStore.db.models.manyToMany.UserWithProducts

@Dao
interface UserWithProductsDao {
    @Query("select * from user_with_products where user_id = :userId and product_id = :productId")
    suspend fun getByUserProduct(productId: Int, userId: Int): UserWithProducts

    @Query("DELETE FROM user_with_products WHERE user_id = :userId")
    suspend fun deleteAllByUser(userId: Int)

    @Insert
    suspend fun insert(userWithProducts: UserWithProducts)

    @Update
    suspend fun update(userWithProducts: UserWithProducts)

    @Delete
    suspend fun delete(userWithProducts: UserWithProducts)
}