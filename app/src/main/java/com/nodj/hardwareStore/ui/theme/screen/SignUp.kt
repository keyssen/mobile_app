package com.nodj.hardwareStore.ui.theme.screen

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.nodj.hardwareStore.ui.theme.MyApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUp(navController: NavController?) {
    Column(modifier = Modifier
        .padding(all = 16.dp)
        .padding(top = 40.dp)) {
        Text(
            text = "Введите логин",
            modifier = Modifier.padding(bottom = 12.dp)
        )
        var login by remember { mutableStateOf("") }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 30.dp),
            value = login,
            onValueChange = { login = it },
            shape = RoundedCornerShape(15.dp),
            placeholder = { Text("Логин") },
        )
        Text(
            text = "Введите пароль",
            modifier = Modifier.padding(bottom = 12.dp)
        )
        var password by remember { mutableStateOf("") }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
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
            Text("Зарегистрироваться")
        }
        Row {
            Text("Уже есть аккаунт? ")
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
    }
}

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SignUpPreview() {
    MyApplicationTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            SignUp(navController = null)
        }
    }
}