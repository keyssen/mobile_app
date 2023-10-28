package com.nodj.hardwareStore.ui.theme.screen

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.nodj.hardwareStore.ui.theme.MyApplicationTheme
import com.nodj.hardwareStore.ui.theme.database.AppDatabase
import com.nodj.hardwareStore.ui.theme.models.User
import com.nodj.hardwareStore.ui.theme.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(navController: NavController?) {
    val context = LocalContext.current
    var user = remember { mutableStateOf<User?>(null) }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            user.value = AppDatabase.getInstance(context).userDao().getByid(1)
        }
    }
    Column(modifier = Modifier
        .padding(all = 16.dp)
        .padding(top = 40.dp)) {
        Text(
            text = "Имя пользователя",
            modifier = Modifier.padding(bottom = 12.dp)
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 30.dp),
            value = user.value?.name ?: "0",
            onValueChange = { user.value?.name = it },
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
            value = password,
            onValueChange = { password = it },
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
            onClick = {}) {
            Text("Сохранить")
        }
        Text(
            text = "Вход",
            modifier = Modifier.clickable {
                navController?.navigate("sign-in") {
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
                navController?.navigate("sign-up") {
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

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProfilePreview() {
    MyApplicationTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Profile(navController = null)
        }
    }
}