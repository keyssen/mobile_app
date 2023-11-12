package com.nodj.hardwareStore.db.repository.repositoryInterface

import androidx.room.Dao
import com.nodj.hardwareStore.db.models.User


@Dao
interface UserRepository {
    suspend fun  getAll(): List<User>

    suspend fun getByid(id: Int): User

    suspend fun insert(user: User)

    suspend fun update(user: User)

    suspend fun delete(user: User)
}