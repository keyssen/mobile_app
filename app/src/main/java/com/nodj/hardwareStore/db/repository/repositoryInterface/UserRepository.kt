package com.nodj.hardwareStore.db.repository.repositoryInterface

import androidx.paging.PagingData
import androidx.room.Dao
import com.nodj.hardwareStore.db.models.User
import kotlinx.coroutines.flow.Flow


@Dao
interface UserRepository {
    fun getAll(): Flow<PagingData<User>>

    suspend fun getByid(id: Int): User

    suspend fun insert(user: User)

    suspend fun update(user: User)

    suspend fun delete(user: User)
}