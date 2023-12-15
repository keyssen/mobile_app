package com.nodj.hardwareStore.db.repository

import com.nodj.hardwareStore.db.dao.UserWithProductsDao
import com.nodj.hardwareStore.db.models.manyToMany.UserWithProducts
import com.nodj.hardwareStore.db.repository.repositoryInterface.UserWithProductsRepository

class OfflineUserWithProductsRepository(private val userWithProductsDao: UserWithProductsDao) :
    UserWithProductsRepository {
    override suspend fun getByUserProduct(productId: Int, userId: Int): UserWithProducts =
        userWithProductsDao.getByUserProduct(productId, userId)

    override suspend fun getAll(): List<UserWithProducts> = userWithProductsDao.getAll()

    override suspend fun deleteAllByUser(userId: Int) = userWithProductsDao.deleteAllByUser(userId)

    override suspend fun insert(userWithProducts: UserWithProducts) {
        userWithProductsDao.insert(userWithProducts)
    }

    suspend fun insertUserWithProducts(userWithProducts: List<UserWithProducts>) =
        userWithProductsDao.insert(*userWithProducts.toTypedArray())

    override suspend fun update(userWithProducts: UserWithProducts) =
        userWithProductsDao.update(userWithProducts)

    override suspend fun delete(userWithProducts: UserWithProducts) =
        userWithProductsDao.delete(userWithProducts)

    suspend fun deleteAll() {
        userWithProductsDao.deleteAll()
    }
}