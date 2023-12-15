package com.nodj.hardwareStore.ui.page.signIn

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.nodj.hardwareStore.db.datastore.PreferencesStore
import com.nodj.hardwareStore.db.repository.repositoryInterface.UserRepository
import com.nodj.hardwareStore.ui.page.profile.UserDetails
import com.nodj.hardwareStore.ui.page.profile.UserUiState

class SignInViewModel(
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

    suspend fun SignIn(context: Context): Boolean {
        if (validateInput()) {
            val user = userRepository.getByNamePassword(
                userUiState.userDetails.name,
                userUiState.userDetails.password
            )
            if (user != null) {
                val store = PreferencesStore(context)
                store.setUsername(user.name)
                userUiState = UserUiState()
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