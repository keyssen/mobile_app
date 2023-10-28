package com.nodj.hardwareStore.ui.theme.product.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nodj.hardwareStore.R
import com.nodj.hardwareStore.ui.theme.MyApplicationTheme
import com.nodj.hardwareStore.ui.theme.database.AppDatabase
import com.nodj.hardwareStore.ui.theme.models.Category
import com.nodj.hardwareStore.ui.theme.models.Order
import com.nodj.hardwareStore.ui.theme.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderList(navController: NavController?) {
    val context = LocalContext.current
    val orders = remember { mutableStateListOf<Order>() }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            orders.clear()
            orders.addAll(AppDatabase.getInstance(context).orderDao().getAll())
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),

        // content padding
        contentPadding = PaddingValues(
            start = 12.dp,
            top = 16.dp,
            end = 12.dp,
            bottom = 16.dp
        ),
        content = {
            items(orders.size) { index ->
                val order = orders[index]
                val orderId = Screen.OrderView.route.replace("{id}", order.id.toString())
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(85.dp)
                        .padding(top = 10.dp)
                        .border(2.dp, Color.Gray, RoundedCornerShape(10.dp))
                        .clickable {
                            navController?.navigate(orderId)
                        },
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ){
                        Text(modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, start = 10.dp),
                            text = "ID: ${order.id}")
                        Text(modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, start = 10.dp),
                            text = "Price: ${10000}")
                    }
                }
            }
        }
    )
}

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OrderListPreview() {
    MyApplicationTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            OrderList(navController = null)
        }
    }
}