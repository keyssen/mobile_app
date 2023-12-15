package com.nodj.hardwareStore.ui.page.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import com.nodj.hardwareStore.db.datastore.PreferencesStore
import com.nodj.hardwareStore.db.models.User
import com.nodj.hardwareStore.db.models.UserRole
import com.nodj.hardwareStore.db.repository.repositoryInterface.UserRepository

class UserViewModel(
    private val userRepository: UserRepository,

    ) : ViewModel() {

    suspend fun Exit(context: Context) {
        val store = PreferencesStore(context)
        store.setUsername("")
        store.setUserrole("")
    }
}

data class UserUiState(
    val userDetails: UserDetails = UserDetails(),
    val isEntryValid: Boolean = false,
    val errorMessage: String = ""
)

data class UserDetails(
    val id: Int = 0,
    val name: String = "",
    val role: UserRole = UserRole.USER,
    val password: String = "",
)

fun UserDetails.toUser(id: Int = 0): User = User(
    id = id,
    name = name,
    role = role,
    password = password
)

fun User.toDetails(): UserDetails = UserDetails(
    id = id,
    name = name,
    role = role,
    password = password
)

fun User.toUiState(isEntryValid: Boolean = false): UserUiState = UserUiState(
    userDetails = this.toDetails(),
    isEntryValid = isEntryValid
)