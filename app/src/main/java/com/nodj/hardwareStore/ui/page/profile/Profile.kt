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
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.nodj.hardwareStore.LiveStore
import com.nodj.hardwareStore.api.ApiStatus
import com.nodj.hardwareStore.common.AppViewModelProvider
import com.nodj.hardwareStore.db.models.User
import com.nodj.hardwareStore.ui.MyApplicationTheme
import com.nodj.hardwareStore.ui.UI.ErrorPlaceholder
import com.nodj.hardwareStore.ui.UI.LoadingPlaceholder
import com.nodj.hardwareStore.ui.navigation.Screen
import com.nodj.hardwareStore.ui.navigation.changeLocationDeprecated
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(
    navController: NavController,
    viewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val user = LiveStore.user.observeAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    fun handleExitClick() {
        scope.launch {
            viewModel.Exit(context)
            if (navController != null) {
                navController.navigate(Screen.SignIn.route)
            }
        }
    }
    when (viewModel.apiStatus) {
        ApiStatus.DONE -> {
            navController?.let {
                Profile(
                    navController = navController, user = user,
                    onExitClick = {
                        handleExitClick()
                    },
                )
            }
        }

        ApiStatus.LOADING -> LoadingPlaceholder()
        else -> ErrorPlaceholder(
            message = viewModel.apiError,
            onBack = { navController?.popBackStack() }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(
    navController: NavController,
    user: State<User?>,
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
            value = user?.value?.name ?: "",
            onValueChange = { },
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
            value = user?.value?.password ?: "",
            onValueChange = {},
            shape = RoundedCornerShape(15.dp),
            placeholder = { Text("Пароль") },
            singleLine = true
        )
        if (user?.value?.id == 0) {
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
                    changeLocationDeprecated(navController, "sign-in")
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