package com.nodj.hardwareStore.api.RestRepository

import android.util.Log
import com.nodj.hardwareStore.api.MyServerService
import com.nodj.hardwareStore.api.model.manyToMany.UserWithProductsRemote
import com.nodj.hardwareStore.api.model.manyToMany.toUserRemote
import com.nodj.hardwareStore.api.model.manyToMany.toUserRemoteForInsert
import com.nodj.hardwareStore.api.model.manyToMany.toUserWithProducts
import com.nodj.hardwareStore.db.models.manyToMany.UserWithProducts
import com.nodj.hardwareStore.db.repository.OfflineUserWithProductsRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.UserWithProductsRepository

class RestUserWithProductsRepository(
    private val service: MyServerService,
    private val dbUserWithProductsRepository: OfflineUserWithProductsRepository,
) : UserWithProductsRepository {
    override suspend fun getAll(): List<UserWithProducts> {
        Log.d(
            RestUserWithProductsRepository::class.simpleName,
            "Get RestUserWithProductsRepository"
        )

        val existUserWithProducts =
            dbUserWithProductsRepository.getAll().associateBy { "${it.productId}-${it.userId}" }
                .toMutableMap()

        service.getAllUserWithProduct()
            .map { it.toUserWithProducts() }
            .forEach { userWithProduct ->
                val existuserWithProduct =
                    existUserWithProducts["${userWithProduct.productId}-${userWithProduct.userId}"]
                if (existuserWithProduct == null) {
                    dbUserWithProductsRepository.insert(userWithProduct)
                } else if (existuserWithProduct != userWithProduct) {
                    dbUserWithProductsRepository.update(userWithProduct)
                }
                existUserWithProducts["${userWithProduct.productId}-${userWithProduct.userId}"] =
                    userWithProduct
            }

        return existUserWithProducts.map { it.value }.sortedBy { "${it.productId}-${it.userId}" }
    }

    override suspend fun getByUserProduct(productId: Int, userId: Int): UserWithProducts {
        try {
            val response = service.getByUserProduct(userId, productId).first().toUserWithProducts()
            return response
            // Handle the response here
        } catch (e: Exception) {
            // Handle exceptions (e.g., 404 error) here
            return UserWithProducts.getEmpty()
        }
    }


    override suspend fun deleteAllByUser(userId: Int) {
        service.getByUserUserWithProducts(userId).forEach {
            service.deleteUserWithProduct(it.id)
        }
        dbUserWithProductsRepository.deleteAll()
    }

    override suspend fun insert(userWithProducts: UserWithProducts) {
        service.createUserWithProduct(userWithProducts.toUserRemoteForInsert())
        dbUserWithProductsRepository.insert(userWithProducts)
    }

    suspend fun getByUserProductRemote(
        productId: Int,
        userId: Int
    ): UserWithProductsRemote {
        try {
            val response = service.getByUserProduct(userId, productId).first()
            return response
            // Handle the response here
        } catch (e: Exception) {
            // Handle exceptions (e.g., 404 error) here
            return UserWithProducts.getEmpty().toUserRemoteForInsert()
        }
    }

    override suspend fun update(userWithProducts: UserWithProducts) {
        val userWithProductsRemote =
            getByUserProductRemote(userWithProducts.productId, userWithProducts.userId)
        val newUserWithProducts = userWithProducts.toUserRemote(userWithProductsRemote.id)
        service.updateUserWithProduct(
            userWithProductsRemote.id,
            newUserWithProducts
        )
        dbUserWithProductsRepository.update(userWithProducts)
    }

    override suspend fun delete(userWithProducts: UserWithProducts) {
        val userWithProductsRemote =
            getByUserProductRemote(userWithProducts.productId, userWithProducts.userId)
        service.deleteUserWithProduct(
            userWithProductsRemote.id
        )
        dbUserWithProductsRepository.delete(userWithProducts)
    }
}