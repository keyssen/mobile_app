package com.nodj.hardwareStore.ui.page.profile

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nodj.hardwareStore.LiveStore
import com.nodj.hardwareStore.db.datastore.PreferencesStore
import com.nodj.hardwareStore.db.models.User
import com.nodj.hardwareStore.db.models.UserRole
import com.nodj.hardwareStore.db.repository.repositoryInterface.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository,

    ) : ViewModel() {

    var userUiState by mutableStateOf(UserUiState())
        private set


    init {
        viewModelScope.launch {
            if (LiveStore.getUserId() != 0) {
                userUiState = userRepository.getByid(LiveStore.getUserId())
                    ?.toUiState(true) ?: UserUiState()
            }
        }
    }

    fun updateUiState(userDetails: UserDetails) {
        userUiState = UserUiState(
            userDetails = userDetails,
            isEntryValid = validateInput(userDetails)
        )
    }

    suspend fun signUp(context: Context) {
        if (validateInput()) {
            userRepository.insert(userUiState.userDetails.toUser())
        }
    }

    suspend fun SignIn(context: Context): Boolean {
        if (validateInput()) {
            val user = userRepository.getByNamePassword(
                userUiState.userDetails.name,
                userUiState.userDetails.password
            )
            if (user != null) {
                val store = PreferencesStore(context)
                store.setUsername(user.name)
                return true
            }
        }
        return false
    }

    private fun validateInput(uiState: UserDetails = userUiState.userDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()
                    && role.value >= 0
                    && password.isNotBlank()
        }
    }
}

data class UserUiState(
    val userDetails: UserDetails = UserDetails(),
    val isEntryValid: Boolean = false
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