package com.nodj.hardwareStore.ui.page.signUp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nodj.hardwareStore.R
import com.nodj.hardwareStore.api.ApiStatus
import com.nodj.hardwareStore.common.AppViewModelProvider
import com.nodj.hardwareStore.db.models.UserRole
import com.nodj.hardwareStore.ui.UI.ErrorPlaceholder
import com.nodj.hardwareStore.ui.UI.LoadingPlaceholder
import com.nodj.hardwareStore.ui.UI.showToast
import com.nodj.hardwareStore.ui.navigation.Screen
import com.nodj.hardwareStore.ui.navigation.changeLocationDeprecated
import com.nodj.hardwareStore.ui.page.signIn.UserDetails
import com.nodj.hardwareStore.ui.page.signIn.UserUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUp(
    navController: NavController,
    viewModel: SignUpViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scope = rememberCoroutineScope()
    val userUiState = viewModel.userUiState
    val context = LocalContext.current
    fun handleSignuPButtonClick() {
        scope.launch {
            viewModel.signUp()
        }
    }
    if (viewModel.signUp) {
        showToast(context, stringResource(R.string.registration))
        changeLocationDeprecated(navController, Screen.SignIn.route)
    }
    if (viewModel.error != 0) {
        showToast(context, stringResource(viewModel.error))
        viewModel.clearError()
    }
    when (viewModel.apiStatus) {
        ApiStatus.DONE -> {
            SignUp(
                navController, userUiState,
                onUpdate = viewModel::updateUiState,
                onSignUpClick = {
                    handleSignuPButtonClick()
                },
            )
        }

        ApiStatus.LOADING -> LoadingPlaceholder()
        else -> ErrorPlaceholder(
            message = viewModel.apiError,
            onBack = { navController.popBackStack() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUp(
    navController: NavController,
    userUiState: UserUiState,
    onUpdate: (UserDetails) -> Unit,
    onSignUpClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(all = 16.dp)
            .padding(top = 40.dp)
    ) {
        Text(
            text = "Введите логин",
            modifier = Modifier.padding(bottom = 12.dp)
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = userUiState.userDetails.name,
            onValueChange = { onUpdate(userUiState.userDetails.copy(name = it)) },
            shape = RoundedCornerShape(15.dp),
            placeholder = { Text("Логин") },
        )
        Text(
            text = "Введите пароль",
            modifier = Modifier.padding(bottom = 12.dp)
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            value = userUiState.userDetails.password,
            onValueChange = { onUpdate(userUiState.userDetails.copy(password = it)) },
            shape = RoundedCornerShape(15.dp),
            placeholder = { Text("пароль") },
            singleLine = true
        )
        Text(
            text = "Выберите роль",
            modifier = Modifier.padding(bottom = 12.dp)
        )
        RoleDropDown(
            userUiState = userUiState,
            roleList = enumValues<UserRole>().toList(),
            onRoleUpdate = {
                onUpdate(userUiState.userDetails.copy(role = it))
            }
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
                .width(100.dp)
                .height(40.dp)
                .align(Alignment.CenterHorizontally),
            onClick = { onSignUpClick() }) {
            Text("Зарегистрироваться")
        }
        Row {
            Text("Уже есть аккаунт? ")
            Text(
                text = "Вход",
                modifier = Modifier.clickable {
                    changeLocationDeprecated(navController, Screen.SignIn.route)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RoleDropDown(
    userUiState: UserUiState,
    roleList: List<UserRole>,
    onRoleUpdate: (UserRole) -> Unit
) {
    var expanded: Boolean by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        modifier = Modifier
            .padding(bottom = 20.dp),
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        TextField(
            value = userUiState.userDetails.role.name,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color.White)
                .exposedDropdownSize()
        ) {
            roleList.forEach { role ->
                DropdownMenuItem(
                    text = {
                        Text(text = role.name)
                    },
                    onClick = {
                        onRoleUpdate(role)
                        expanded = false
                    }
                )
            }
        }
    }
}