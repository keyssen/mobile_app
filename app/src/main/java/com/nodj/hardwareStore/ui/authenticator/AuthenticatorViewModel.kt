package com.nodj.hardwareStore.ui.authenticator

import androidx.lifecycle.ViewModel
import com.nodj.hardwareStore.db.models.User
import com.nodj.hardwareStore.db.repository.repositoryInterface.UserRepository

class AuthenticatorViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    suspend fun findUserByName(name: String): User? {
        return userRepository.getByName(name)
    }
}
