package com.nodj.myapplication.ui.theme.screen

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.nodj.myapplication.ui.theme.MyApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(navController: NavController?) {
    Column(modifier = Modifier
        .padding(all = 16.dp)
        .padding(top = 40.dp)) {
        Text(
            text = "Имя пользователя",
            modifier = Modifier.padding(bottom = 12.dp)
        )
        var name by remember { mutableStateOf("") }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 30.dp),
            value = name,
            onValueChange = { name = it },
            shape = RoundedCornerShape(15.dp),
            placeholder = { Text("Имя") },
        )
        Text(
            text = "Пароль пользователя",
            modifier = Modifier.padding(bottom = 12.dp)
        )
        var password by remember { mutableStateOf("") }
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
        Text(
            text = "Логин пользователя",
            modifier = Modifier.padding(bottom = 12.dp)
        )
        var login by remember { mutableStateOf("") }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            value = login,
            onValueChange = { login = it },
            shape = RoundedCornerShape(15.dp),
            placeholder = { Text("логин") },
            singleLine = true
        )
        Row {
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
        }
        Row {
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