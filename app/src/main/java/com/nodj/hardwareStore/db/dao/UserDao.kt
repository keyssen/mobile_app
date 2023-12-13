package com.nodj.hardwareStore.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nodj.hardwareStore.db.models.User


@Dao
interface UserDao {
    @Query("select * from users order by name collate nocase asc")
    fun getAll(): PagingSource<Int, User>

    @Query("SELECT * FROM users WHERE name = :name AND password = :password LIMIT 1")
    suspend fun getByNamePassword(name: String, password: String): User?

    @Query("select * from users where name = :name")
    suspend fun getByName(name: String): User?

    @Query("select * from users where user_id = :id")
    suspend fun getByid(id: Int): User?

    @Insert
    suspend fun insert(vararg user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("DELETE FROM users")
    suspend fun deleteAll()
}