package com.nodj.hardwareStore.ui.authenticator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.nodj.hardwareStore.R
import com.nodj.hardwareStore.common.MyViewModel
import com.nodj.hardwareStore.db.models.User
import com.nodj.hardwareStore.db.repository.repositoryInterface.UserRepository

class AuthenticatorViewModel(
    private val userRepository: UserRepository
) : MyViewModel() {

    var user by mutableStateOf<User?>(null)
        private set

    var error by mutableStateOf(0)
        private set

    suspend fun findUserByName(name: String) {
        runInScope(
            actionSuccess = {
                user = userRepository.getByName(name)
            },
            actionError = {
                error = R.string.error_404
            }
        )
    }
}