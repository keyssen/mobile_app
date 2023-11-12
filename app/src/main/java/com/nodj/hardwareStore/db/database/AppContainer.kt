package com.nodj.hardwareStore.db.database

import android.content.Context
import com.nodj.hardwareStore.db.repository.OfflineCategoryRepository
import com.nodj.hardwareStore.db.repository.OfflineOrderRepository
import com.nodj.hardwareStore.db.repository.OfflineOrderWithProductsRepository
import com.nodj.hardwareStore.db.repository.OfflineProductRepository
import com.nodj.hardwareStore.db.repository.OfflineUserRepository
import com.nodj.hardwareStore.db.repository.OfflineUserWithProductsRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.CategoryRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.OrderRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.OrderWithProductsRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.ProductRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.UserRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.UserWithProductsRepository

interface AppContainer {
    val categoryRepository: CategoryRepository
    val orderRepository: OrderRepository
    val orderWithProductsRepository: OrderWithProductsRepository
    val productRepository: ProductRepository
    val userRepository: UserRepository
    val userWithProductsRepository: UserWithProductsRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val categoryRepository: CategoryRepository by lazy {
        OfflineCategoryRepository(AppDatabase.getInstance(context).categoryDao())
    }
    override val orderRepository: OrderRepository by lazy {
        OfflineOrderRepository(AppDatabase.getInstance(context).orderDao())
    }
    override val orderWithProductsRepository: OrderWithProductsRepository by lazy {
        OfflineOrderWithProductsRepository(AppDatabase.getInstance(context).orderWithProductsDao())
    }
    override val productRepository: ProductRepository by lazy {
        OfflineProductRepository(AppDatabase.getInstance(context).productDao())
    }
    override val userRepository: UserRepository by lazy {
        OfflineUserRepository(AppDatabase.getInstance(context).userDao())
    }
    override val userWithProductsRepository: UserWithProductsRepository by lazy {
        OfflineUserWithProductsRepository(AppDatabase.getInstance(context).userWithProductsDao())
    }

    companion object {
        const val TIMEOUT = 5000L
    }
}