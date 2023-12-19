package com.nodj.hardwareStore.db.repository.repositoryInterface

import androidx.room.Dao
import com.nodj.hardwareStore.db.models.manyToMany.UserWithProducts

@Dao
interface UserWithProductsRepository {
    suspend fun getByUserProduct(productId: Int, userId: Int): UserWithProducts?

    suspend fun deleteAllByUser(userId: Int)

    suspend fun insert(userWithProducts: UserWithProducts)

    suspend fun update(userWithProducts: UserWithProducts)

    suspend fun delete(userWithProducts: UserWithProducts)
}