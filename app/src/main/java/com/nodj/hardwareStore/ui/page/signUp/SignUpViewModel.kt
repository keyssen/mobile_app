package com.nodj.hardwareStore.ui.page.signUp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.nodj.hardwareStore.db.repository.repositoryInterface.UserRepository
import com.nodj.hardwareStore.ui.page.profile.UserDetails
import com.nodj.hardwareStore.ui.page.profile.UserUiState
import com.nodj.hardwareStore.ui.page.profile.toUser

class SignUpViewModel(
    private val userRepository: UserRepository,

    ) : ViewModel() {

    var userUiState by mutableStateOf(UserUiState())
        private set

    fun updateUiState(userDetails: UserDetails) {
        userUiState = UserUiState(
            userDetails = userDetails,
            isEntryValid = validateInput(userDetails)
        )
    }

    suspend fun signUp(): Boolean {
        if (validateInput()) {
            if (userRepository.getByName(userUiState.userDetails.name) === null) {
                userRepository.insert(userUiState.userDetails.toUser())
                userUiState = UserUiState()
                return true;
            } else {
                userUiState = UserUiState(
                    userDetails = userUiState.userDetails,
                    errorMessage = "Пользователь с таким логином уже существует",
                    isEntryValid = false
                )
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