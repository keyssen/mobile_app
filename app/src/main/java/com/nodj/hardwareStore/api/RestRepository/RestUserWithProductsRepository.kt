package com.nodj.hardwareStore.api.RestRepository

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
    override suspend fun getByUserProduct(productId: Int, userId: Int): UserWithProducts {
        return service.getByUserProduct(userId, productId).first().toUserWithProducts()
    }

    override suspend fun deleteAllByUser(userId: Int) {
        service.getByUserUserWithProducts(userId).forEach {
            service.deleteUserWithProduct(it.id)
        }
        dbUserWithProductsRepository.deleteAll()
    }

    override suspend fun insert(userWithProducts: UserWithProducts) {
        if (service.getByUserProduct(userWithProducts.productId, userWithProducts.userId).isEmpty()
        ) {
            dbUserWithProductsRepository.insert(
                service.createUserWithProduct(userWithProducts.toUserRemoteForInsert())
                    .toUserWithProducts()
            )
        }
    }

    suspend fun getByUserProductRemote(
        productId: Int,
        userId: Int
    ): UserWithProductsRemote {
        return service.getByUserProduct(userId, productId).first()
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