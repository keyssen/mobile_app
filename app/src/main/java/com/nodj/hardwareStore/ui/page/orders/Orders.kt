package com.nodj.hardwareStore.ui.page.orders

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.nodj.hardwareStore.db.theme.order.OrderList
import com.nodj.hardwareStore.ui.MyApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Orders(navController: NavController) {
    Column(){
        OrderList(navController)
    }
}

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FavoritesPreview() {
    MyApplicationTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
//            Orders(navController = null)
        }
    }
}