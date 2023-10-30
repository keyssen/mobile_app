package com.nodj.hardwareStore.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nodj.hardwareStore.db.models.User


@Dao
interface UserDao {
    @Query("select * from users order by name collate nocase asc")
    suspend fun  getAll(): List<User>

    @Query("select * from users where user_id = :id")
    suspend fun getByid(id: Int): User

    @Insert
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)
}