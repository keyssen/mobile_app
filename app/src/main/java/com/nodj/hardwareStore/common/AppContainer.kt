package com.nodj.hardwareStore.common

import android.content.Context
import com.nodj.hardwareStore.api.MyServerService
import com.nodj.hardwareStore.api.RestRepository.RestCategoryRepository
import com.nodj.hardwareStore.api.RestRepository.RestOrderRepository
import com.nodj.hardwareStore.api.RestRepository.RestProductRepository
import com.nodj.hardwareStore.api.RestRepository.RestUserRepository
import com.nodj.hardwareStore.api.RestRepository.RestUserWithProductsRepository
import com.nodj.hardwareStore.db.database.AppDatabase
import com.nodj.hardwareStore.db.remotekeys.dao.OfflineRemoteKeyRepository
import com.nodj.hardwareStore.db.repository.OfflineCategoryRepository
import com.nodj.hardwareStore.db.repository.OfflineOrderRepository
import com.nodj.hardwareStore.db.repository.OfflineOrderWithProductsRepository
import com.nodj.hardwareStore.db.repository.OfflineProductRepository
import com.nodj.hardwareStore.db.repository.OfflineUserRepository
import com.nodj.hardwareStore.db.repository.OfflineUserWithProductsRepository

interface AppContainer {
    val restCategoryRepository: RestCategoryRepository
    val restUserRepository: RestUserRepository
    val restProductRepository: RestProductRepository
    val restUserWithProductsRepository: RestUserWithProductsRepository
    val restOrderRepository: RestOrderRepository
    val service: MyServerService

    companion object {
        const val TIMEOUT = 5000L
        const val LIMIT = 10
    }
}

class AppDataContainer(private val context: Context) : AppContainer {
    private val categoryRepository: OfflineCategoryRepository by lazy {
        OfflineCategoryRepository(AppDatabase.getInstance(context).categoryDao())
    }
    private val userRepository: OfflineUserRepository by lazy {
        OfflineUserRepository(AppDatabase.getInstance(context).userDao())
    }
    private val productRepository: OfflineProductRepository by lazy {
        OfflineProductRepository(AppDatabase.getInstance(context).productDao())
    }
    private val orderRepository: OfflineOrderRepository by lazy {
        OfflineOrderRepository(AppDatabase.getInstance(context).orderDao())
    }
    private val userWithProductsRepository: OfflineUserWithProductsRepository by lazy {
        OfflineUserWithProductsRepository(AppDatabase.getInstance(context).userWithProductsDao())
    }
    private val orderWithProductsRepository: OfflineOrderWithProductsRepository by lazy {
        OfflineOrderWithProductsRepository(AppDatabase.getInstance(context).orderWithProductsDao())
    }
    private val remoteKeyRepository: OfflineRemoteKeyRepository by lazy {
        OfflineRemoteKeyRepository(AppDatabase.getInstance(context).remoteKeysDao())
    }
    override val service: MyServerService by lazy {
        MyServerService.getInstance()
    }
    override val restOrderRepository: RestOrderRepository by lazy {
        RestOrderRepository(
            MyServerService.getInstance(),
            orderRepository,
            orderWithProductsRepository,
        )
    }
    override val restCategoryRepository: RestCategoryRepository by lazy {
        RestCategoryRepository(
            MyServerService.getInstance(),
            categoryRepository,
            remoteKeyRepository,
            AppDatabase.getInstance(context)
        )
    }
    override val restUserRepository: RestUserRepository by lazy {
        RestUserRepository(
            MyServerService.getInstance(),
            userRepository,
        )
    }
    override val restProductRepository: RestProductRepository by lazy {
        RestProductRepository(
            MyServerService.getInstance(),
            productRepository,
            userWithProductsRepository,
            remoteKeyRepository,
            AppDatabase.getInstance(context)
        )
    }
    override val restUserWithProductsRepository: RestUserWithProductsRepository by lazy {
        RestUserWithProductsRepository(
            MyServerService.getInstance(),
            userWithProductsRepository
        )
    }
}