package com.nodj.hardwareStore.ui.page.signIn

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.nodj.hardwareStore.R
import com.nodj.hardwareStore.common.MyViewModel
import com.nodj.hardwareStore.db.datastore.PreferencesStore
import com.nodj.hardwareStore.db.models.User
import com.nodj.hardwareStore.db.models.UserRole
import com.nodj.hardwareStore.db.repository.repositoryInterface.UserRepository

class SignInViewModel(
    private val userRepository: UserRepository,
) : MyViewModel() {

    var userUiState by mutableStateOf(UserUiState())
        private set

    var error by mutableStateOf(0)
        private set

    var signIn by mutableStateOf(false)

    fun updateUiState(userDetails: UserDetails) {
        userUiState = UserUiState(
            userDetails = userDetails,
            isEntryValid = validateInput(userDetails)
        )
    }

    fun clearError() {
        error = 0
    }

    suspend fun SignIn(context: Context) {
        if (validateInput()) {
            runInScope(
                actionSuccess = {
                    val user = userRepository.getByNamePassword(
                        userUiState.userDetails.name,
                        userUiState.userDetails.password
                    )
                    if (user != null) {
                        val store = PreferencesStore(context)
                        store.setUsername(user.name)
                        signIn = true
                        userUiState = UserUiState()
                    } else {
                        signIn = false
                        error = R.string.error_authentication
                    }
                },
                actionError = {
                    signIn = false
                    error = R.string.error_404
                }
            )
        }
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
