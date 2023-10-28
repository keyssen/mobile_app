package com.nodj.hardwareStore.ui.theme.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nodj.hardwareStore.ui.theme.models.Category
import com.nodj.hardwareStore.ui.theme.models.User
import kotlinx.coroutines.flow.Flow


@Dao
interface CategoryDao {
    @Query("select * from categories order by name collate nocase asc")
    suspend fun  getAll(): List<Category>

    @Query("select * from categories where category_id = :id")
    suspend fun getByid(id: Int): Category

    @Insert
    suspend fun insert(category: Category)

    @Update
    suspend fun update(category: Category)

    @Delete
    suspend fun delete(category: Category)
}