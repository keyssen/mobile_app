package com.nodj.hardwareStore.db.remotekeys.dao;

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.nodj.hardwareStore.db.models.Category
import com.nodj.hardwareStore.db.models.Product
import com.nodj.hardwareStore.db.models.User
import com.nodj.hardwareStore.db.models.helperModels.AdvancedProduct

enum class RemoteKeyType(private val type: String) {
    CATEGORY(Category::class.simpleName ?: "Category"),
    USER(User::class.simpleName ?: "User"),
    PRODUCT(Product::class.simpleName ?: "Product"),
    ADVANCEDPRODUCT(AdvancedProduct::class.simpleName ?: "AdvancedProduct");

    @TypeConverter
    fun toRemoteKeyType(value: String) = RemoteKeyType.values().first { it.type == value }

    @TypeConverter
    fun fromRemoteKeyType(value: RemoteKeyType) = value.type
}

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val entityId: Int,
    @TypeConverters(RemoteKeyType::class)
    val type: RemoteKeyType,
    val prevKey: Int?,
    val nextKey: Int?
)
