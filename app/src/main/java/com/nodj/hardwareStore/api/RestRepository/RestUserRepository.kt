package com.nodj.hardwareStore.api.RestRepository

import com.nodj.hardwareStore.api.MyServerService
import com.nodj.hardwareStore.api.model.toUser
import com.nodj.hardwareStore.api.model.toUserRemote
import com.nodj.hardwareStore.db.database.AppDatabase
import com.nodj.hardwareStore.db.models.User
import com.nodj.hardwareStore.db.remotekeys.dao.OfflineRemoteKeyRepository
import com.nodj.hardwareStore.db.repository.OfflineUserRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.UserRepository

class RestUserRepository(
    private val service: MyServerService,
    private val dbUserRepository: OfflineUserRepository,
    private val dbRemoteKeyRepository: OfflineRemoteKeyRepository,
    private val database: AppDatabase
) : UserRepository {
    override suspend fun getByName(name: String): User? {
        return if (service.getUserByName(name).count() == 1) service.getUserByName(name).get(0)
            .toUser() else null
    }

    override suspend fun getByNamePassword(name: String, password: String): User? {
        val user = service.getUserByNamePassword(name, password)?.get(0)?.toUser()
        if (dbUserRepository.getByNamePassword(name, password) == null && user != null) {
            dbUserRepository.insert(user)
        }
        return user
    }

    override suspend fun getByid(id: Int): User = service.getUser(id).toUser()

    override suspend fun insert(user: User) {
        dbUserRepository.insert(service.createUser(user.toUserRemote()).toUser())
    }

    override suspend fun update(user: User) {
        dbUserRepository.update(service.updateUser(user.id, user.toUserRemote()).toUser())
    }

    override suspend fun delete(user: User) {
        dbUserRepository.delete(service.deleteUser(user.id).toUser())
    }
}