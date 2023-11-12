package com.nodj.hardwareStore.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nodj.hardwareStore.db.models.Category
import kotlinx.coroutines.flow.Flow


@Dao
interface CategoryDao {
    @Query("select * from categories order by name collate nocase asc")
    fun  getAll(): Flow<List<Category>>

    @Query("select * from categories where category_id = :id")
    fun getByid(id: Int): Flow<Category>

    @Insert
    suspend fun insert(category: Category)

    @Update
    suspend fun update(category: Category)

    @Delete
    suspend fun delete(category: Category)
}