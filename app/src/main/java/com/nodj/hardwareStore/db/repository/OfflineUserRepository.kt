package com.nodj.hardwareStore.db.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.nodj.hardwareStore.common.AppContainer
import com.nodj.hardwareStore.db.dao.UserDao
import com.nodj.hardwareStore.db.models.User
import com.nodj.hardwareStore.db.repository.repositoryInterface.UserRepository
import kotlinx.coroutines.flow.Flow

class OfflineUserRepository(private val userDao: UserDao) : UserRepository {
    override fun getAll(): Flow<PagingData<User>> = Pager(
        config = PagingConfig(
            pageSize = AppContainer.LIMIT,
            enablePlaceholders = false
        ),
        pagingSourceFactory = userDao::getAll
    ).flow

    override suspend fun getByName(name: String): User? = userDao.getByName(name)

    override suspend fun getByNamePassword(name: String, password: String): User? =
        userDao.getByNamePassword(name, password)

    fun getAllUsersPagingSource(): PagingSource<Int, User> = userDao.getAll()

    override suspend fun getByid(id: Int): User? = userDao.getByid(id)

    override suspend fun insert(user: User) = userDao.insert(user)

    suspend fun insertUsers(users: List<User>) =
        userDao.insert(*users.toTypedArray())

    override suspend fun update(user: User) = userDao.update(user)

    override suspend fun delete(user: User) = userDao.delete(user)

    suspend fun deleteAll() = userDao.deleteAll()
}