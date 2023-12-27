package com.nodj.hardwareStore.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nodj.hardwareStore.db.dao.CategoryDao
import com.nodj.hardwareStore.db.dao.OrderDao
import com.nodj.hardwareStore.db.dao.OrderWithProductsDao
import com.nodj.hardwareStore.db.dao.ProductDao
import com.nodj.hardwareStore.db.dao.UserDao
import com.nodj.hardwareStore.db.dao.UserWithProductsDao
import com.nodj.hardwareStore.db.models.Category
import com.nodj.hardwareStore.db.models.Order
import com.nodj.hardwareStore.db.models.Product
import com.nodj.hardwareStore.db.models.User
import com.nodj.hardwareStore.db.models.manyToMany.OrderWithProducts
import com.nodj.hardwareStore.db.models.manyToMany.UserWithProducts
import com.nodj.hardwareStore.db.remotekeys.dao.RemoteKeys
import com.nodj.hardwareStore.db.remotekeys.dao.RemoteKeysDao

@Database(
    entities = [User::class, Product::class, Category::class, UserWithProducts::class, Order::class, OrderWithProducts::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    abstract fun productDao(): ProductDao

    abstract fun remoteKeysDao(): RemoteKeysDao

    abstract fun categoryDao(): CategoryDao

    abstract fun userWithProductsDao(): UserWithProductsDao

    abstract fun orderDao(): OrderDao

    abstract fun orderWithProductsDao(): OrderWithProductsDao

    companion object {
        private const val DB_NAME: String = "pmy-db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(appContext: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    appContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}