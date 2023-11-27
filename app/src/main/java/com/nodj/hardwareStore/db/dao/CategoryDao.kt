package com.nodj.hardwareStore.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nodj.hardwareStore.db.models.Category


@Dao
interface CategoryDao {
    @Query("select * from categories order by name collate nocase asc")
    fun getAllCategories(): PagingSource<Int, Category>

    @Query("select * from categories order by name collate nocase asc")
    suspend fun getAll(): List<Category>

    @Query("select * from categories where category_id = :id")
    suspend fun getByid(id: Int): Category

    @Insert
    suspend fun insert(vararg category: Category)

    @Update
    suspend fun update(category: Category)

    @Delete
    suspend fun delete(category: Category)

    @Query("DELETE FROM categories")
    suspend fun deleteAll()
}