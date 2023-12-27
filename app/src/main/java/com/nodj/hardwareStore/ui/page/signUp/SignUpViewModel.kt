package com.nodj.hardwareStore.ui.page.signUp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.nodj.hardwareStore.R
import com.nodj.hardwareStore.common.MyViewModel
import com.nodj.hardwareStore.db.repository.repositoryInterface.UserRepository
import com.nodj.hardwareStore.ui.page.signIn.UserDetails
import com.nodj.hardwareStore.ui.page.signIn.UserUiState
import com.nodj.hardwareStore.ui.page.signIn.toUser

class SignUpViewModel(
    private val userRepository: UserRepository,
) : MyViewModel() {

    var userUiState by mutableStateOf(UserUiState())
        private set

    var error by mutableStateOf(0)
        private set

    var signUp by mutableStateOf(false)

    fun updateUiState(userDetails: UserDetails) {
        userUiState = UserUiState(
            userDetails = userDetails,
            isEntryValid = validateInput(userDetails)
        )
    }

    fun clearError() {
        error = 0
    }

    suspend fun signUp() {
        if (validateInput()) {
            runInScope(
                actionSuccess = {
                    if (userRepository.getByName(userUiState.userDetails.name) === null) {
                        userRepository.insert(userUiState.userDetails.toUser())
                        userUiState = UserUiState()
                        signUp = true;
                    } else {
                        signUp = false;
                        error = R.string.error_registration
                        userUiState = UserUiState(
                            userDetails = userUiState.userDetails,
                            errorMessage = "",
                            isEntryValid = false
                        )
                    }
                },
                actionError = {
                    signUp = false
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