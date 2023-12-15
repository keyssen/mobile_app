package com.nodj.hardwareStore.db.repository.repositoryInterface

import androidx.room.Dao
import com.nodj.hardwareStore.db.models.User


@Dao
interface UserRepository {
    suspend fun getByName(name: String): User?

    suspend fun getByNamePassword(name: String, password: String): User?

    suspend fun getByid(id: Int): User?

    suspend fun insert(user: User)

    suspend fun update(user: User)

    suspend fun delete(user: User)
}