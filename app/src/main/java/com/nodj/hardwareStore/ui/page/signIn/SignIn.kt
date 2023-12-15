package com.nodj.hardwareStore.ui.page.signIn

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nodj.hardwareStore.common.AppViewModelProvider
import com.nodj.hardwareStore.ui.MyApplicationTheme
import com.nodj.hardwareStore.ui.navigation.Screen
import com.nodj.hardwareStore.ui.navigation.changeLocationDeprecated
import com.nodj.hardwareStore.ui.page.profile.UserDetails
import com.nodj.hardwareStore.ui.page.profile.UserUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignIn(
    navController: NavController?,
    viewModel: SignInViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val userUiState = viewModel.userUiState
    fun handleSignInButtonClick() {
        scope.launch {
            if (viewModel.SignIn(context)) {
                if (navController != null) {
                    changeLocationDeprecated(navController, Screen.Profile.route)
                }
            }
        }
    }
    navController?.let {
        SignIn(
            it, userUiState,
            onUpdate = viewModel::updateUiState,
            onSignInClick = {
                handleSignInButtonClick()
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignIn(
    navController: NavController,
    userUiState: UserUiState,
    onUpdate: (UserDetails) -> Unit,
    onSignInClick: () -> Unit,
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
                .fillMaxWidth()
                .padding(bottom = 30.dp),
            value = userUiState.userDetails.name,
            onValueChange = { onUpdate(userUiState.userDetails.copy(name = it)) },
            shape = RoundedCornerShape(15.dp),
            placeholder = { Text("Имя") },
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
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
                .width(100.dp)
                .height(40.dp)
                .align(Alignment.CenterHorizontally),
            onClick = { onSignInClick() }) {
            Text("Войти")
        }
        Row {
            Text("Ещё нет аккаунта? ")
            Text(
                text = "Регистрация",
                modifier = Modifier.clickable {
                    changeLocationDeprecated(navController, Screen.SignUp.route)
                }
            )
        }
    }
}

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SignInPreview() {
    MyApplicationTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            SignIn(navController = null)
        }
    }
}