package com.nodj.hardwareStore.db.repository

import com.nodj.hardwareStore.db.dao.UserDao
import com.nodj.hardwareStore.db.dao.UserWithProductsDao
import com.nodj.hardwareStore.db.models.manyToMany.UserWithProducts
import com.nodj.hardwareStore.db.repository.repositoryInterface.UserRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.UserWithProductsRepository

class OfflineUserWithProductsRepository(private val userWithProductsDao: UserWithProductsDao) :
    UserWithProductsRepository {
    override suspend fun getByUserProduct(productId: Int, userId: Int): UserWithProducts  = userWithProductsDao.getByUserProduct(productId, userId)
    override suspend fun deleteAllByUser(userId: Int) = userWithProductsDao.deleteAllByUser(userId)

    override suspend fun insert(userWithProducts: UserWithProducts){
        val UserWithProducts = getByUserProduct(userWithProducts.productId, userWithProducts.userId)
        if (UserWithProducts == null) {
            userWithProductsDao.insert(userWithProducts)
        }
    }

    override suspend fun update(userWithProducts: UserWithProducts) = userWithProductsDao.update(userWithProducts)

    override suspend fun delete(userWithProducts: UserWithProducts) = userWithProductsDao.delete(userWithProducts)
}