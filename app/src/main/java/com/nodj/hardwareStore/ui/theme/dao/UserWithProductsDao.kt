package com.nodj.hardwareStore.ui.theme.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.nodj.hardwareStore.ui.theme.models.manyToMany.UserWithProducts

@Dao
interface UserWithProductsDao {
    @Insert
    suspend fun insert(userWithProducts: UserWithProducts)

    @Update
    suspend fun update(userWithProducts: UserWithProducts)

    @Delete
    suspend fun delete(userWithProducts: UserWithProducts)
}