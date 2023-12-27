package com.nodj.hardwareStore.db.repository

import com.nodj.hardwareStore.db.dao.UserDao
import com.nodj.hardwareStore.db.models.User
import com.nodj.hardwareStore.db.repository.repositoryInterface.UserRepository

class OfflineUserRepository(private val userDao: UserDao) : UserRepository {
    override suspend fun getByName(name: String): User? = userDao.getByName(name)

    override suspend fun getByNamePassword(name: String, password: String): User? =
        userDao.getByNamePassword(name, password)

    override suspend fun getByid(id: Int): User? = userDao.getByid(id)

    override suspend fun insert(user: User) = userDao.insert(user)

    override suspend fun update(user: User) = userDao.update(user)

    override suspend fun delete(user: User) = userDao.delete(user)

    suspend fun deleteAll() = userDao.deleteAll()
}