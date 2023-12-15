package com.nodj.hardwareStore.ui.page.profile

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.nodj.hardwareStore.LiveStore
import com.nodj.hardwareStore.common.AppViewModelProvider
import com.nodj.hardwareStore.ui.MyApplicationTheme
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(
    navController: NavController,
    viewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val userUiState = viewModel.userUiState
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    fun handleExitClick() {
        scope.launch {
            viewModel.Exit(context)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.refresh()
    }
    Profile(
        navController, userUiState,
        onUpdate = viewModel::updateUiState,
        onExitClick = {
            handleExitClick()
        },
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(
    navController: NavController,
    userUiState: UserUiState,
    onUpdate: (UserDetails) -> Unit,
    onExitClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(all = 16.dp)
            .padding(top = 40.dp)
    ) {
        Text(
            text = "Имя пользователя",
            modifier = Modifier.padding(bottom = 12.dp)
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 30.dp),
            value = userUiState.userDetails.name,
            onValueChange = { onUpdate(userUiState.userDetails.copy(name = it)) },
            shape = RoundedCornerShape(15.dp),
            placeholder = { Text("Имя") },
        )
        Text(
            text = "Пароль пользователя",
            modifier = Modifier.padding(bottom = 12.dp)
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 30.dp),
            value = userUiState.userDetails.password,
            onValueChange = { onUpdate(userUiState.userDetails.copy(password = it)) },
            shape = RoundedCornerShape(15.dp),
            placeholder = { Text("Пароль") },
            singleLine = true
        )
        if (LiveStore.getUserId() == 0) {
            Text(
                text = "Вход",
                modifier = Modifier.clickable {
                    navController.navigate("sign-in") {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
            Text(
                text = "Регистрация",
                modifier = Modifier.clickable {
                    navController.navigate("sign-up") {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        } else {
            Text(
                text = "Выйти",
                modifier = Modifier.clickable {
                    onExitClick()
                    navController.navigate("sign-in") {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

    }
}

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProfilePreview() {
    MyApplicationTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
        }
    }
}