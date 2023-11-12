package com.nodj.hardwareStore.db.repository

import com.nodj.hardwareStore.db.dao.UserDao
import com.nodj.hardwareStore.db.models.User
import com.nodj.hardwareStore.db.repository.repositoryInterface.UserRepository

class OfflineUserRepository(private val userDao: UserDao) : UserRepository {
    override suspend fun getAll(): List<User> = userDao.getAll()

    override suspend fun getByid(id: Int): User = userDao.getByid(id)

    override suspend fun insert(user: User) = userDao.insert(user)

    override suspend fun update(user: User) = userDao.update(user)

    override suspend fun delete(user: User) = userDao.delete(user)
}