package com.nodj.hardwareStore.db.database

import android.content.Context
import android.graphics.Bitmap
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
import com.nodj.hardwareStore.db.models.UserRole
import com.nodj.hardwareStore.db.models.manyToMany.OrderWithProducts
import com.nodj.hardwareStore.db.models.manyToMany.UserWithProducts
import com.nodj.hardwareStore.db.remotekeys.dao.RemoteKeys
import com.nodj.hardwareStore.db.remotekeys.dao.RemoteKeysDao
import java.util.Date

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

        private suspend fun populateDatabase() {
            INSTANCE?.let { database ->
                val userDao = database.userDao()
                val user1 = User(1, "Name", UserRole.USER, "password")
                userDao.insert(user1)
                val categoryDao = database.categoryDao()
                val category1 = Category(1, "Смартфон")
                val category2 = Category(2, "Телефон")
                categoryDao.insert(category1)
                categoryDao.insert(category2)
                val productDao = database.productDao()

                val width = 100
                val height = 100
                val colors =
                    IntArray(width * height) { 0xFF000000.toInt() } // Initialize with black color
                val offset = 0
                val stride = width
                val config = Bitmap.Config.ARGB_8888

                val bitmap = Bitmap.createBitmap(colors, offset, stride, width, height, config)
//                val image = Image()
//                val a = MainActivity()
                val userWithProductsDao = database.userWithProductsDao()
                val userWithProducts1 = UserWithProducts(1, 1, 3)
                val userWithProducts2 = UserWithProducts(1, 2, 2)
                val userWithProducts3 = UserWithProducts(1, 3, 5)
                userWithProductsDao.insert(userWithProducts1)
                userWithProductsDao.insert(userWithProducts2)
                userWithProductsDao.insert(userWithProducts3)
                val orderDao = database.orderDao()
                val order1 = Order(1, Date(), 1)
                val order2 = Order(2, Date(), 1)
                val order3 = Order(3, Date(), 1)
                val order4 = Order(4, Date(), 1)
                orderDao.insert(order1)
                orderDao.insert(order2)
                orderDao.insert(order3)
                orderDao.insert(order4)
                val orderWithProductsDao = database.orderWithProductsDao()
                val orderWithProductsDao1 = OrderWithProducts(1, 1, 3, 1000.0)
                val orderWithProductsDao2 = OrderWithProducts(1, 2, 2, 2000.0)
                val orderWithProductsDao3 = OrderWithProducts(1, 3, 5, 3000.0)

                val orderWithProductsDao4 = OrderWithProducts(2, 1, 3, 1000.0)

                val orderWithProductsDao5 = OrderWithProducts(3, 1, 3, 1000.0)
                val orderWithProductsDao6 = OrderWithProducts(3, 2, 2, 2000.0)

                val orderWithProductsDao7 = OrderWithProducts(4, 1, 3, 1000.0)
                orderWithProductsDao.insert(orderWithProductsDao1)
                orderWithProductsDao.insert(orderWithProductsDao2)
                orderWithProductsDao.insert(orderWithProductsDao3)
                orderWithProductsDao.insert(orderWithProductsDao4)
                orderWithProductsDao.insert(orderWithProductsDao5)
                orderWithProductsDao.insert(orderWithProductsDao6)
                orderWithProductsDao.insert(orderWithProductsDao7)
            }
        }

        fun getInstance(appContext: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    appContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
//                    .addCallback(object : Callback() {
//                        override fun onCreate(db: SupportSQLiteDatabase) {
//                            super.onCreate(db)
//                            CoroutineScope(Dispatchers.IO).launch {
//                                populateDatabase()
//                            }
//                        }
//                    })
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}