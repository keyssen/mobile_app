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
import com.nodj.hardwareStore.ui.theme.models.Product
import com.nodj.hardwareStore.ui.theme.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductList(navController: NavController?, type: String = "catalog", categoryId: Int = -1) {
    val context = LocalContext.current
    val products = remember { mutableStateListOf<Product>() }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            products.clear()
            products.addAll(AppDatabase.getInstance(context).productDao().getAll())
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
            items(products.size) { index ->
                val product = products[index]
                val studentId = Screen.ProductView.route.replace("{id}", product.id.toString())
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(top = 10.dp)
                        .border(2.dp, Color.Gray, RoundedCornerShape(10.dp))
                        .clickable {
                            navController?.navigate(studentId)
                        },
                ) {
                    Image(
                        modifier = Modifier
                            .width(110.dp)
                            .height(160.dp)
                            .padding(start = 10.dp),
                        bitmap = ImageBitmap.imageResource(product.imageId),
                        contentDescription = "Продукт"
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ){
                        Row(modifier = Modifier
                            .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            if (type == "catalog"){
                                Button(
                                    modifier = Modifier
                                        .padding(all = 10.dp)
                                        .width(100.dp)
                                        .height(40.dp),
                                    onClick = { navController?.navigate(studentId) }) {
                                    Text("Купить")
                                }
                            } else if (type == "cart"){
                                Button(
                                    modifier = Modifier
                                        .padding(top = 10.dp, bottom = 10.dp)
                                        .width(65.dp)
                                        .height(40.dp),
                                    onClick = { navController?.navigate(studentId) }) {
                                    Icon(
                                        imageVector = Icons.Filled.Add,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(20.dp)
                                    )
                                }
                                Button(
                                    modifier = Modifier
                                        .padding(top = 10.dp, bottom = 10.dp)
                                        .width(65.dp)
                                        .height(40.dp),
                                    onClick = { navController?.navigate(studentId) }) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.minus),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(20.dp)
                                    )
                                }
                                Button(
                                    modifier = Modifier
                                        .padding(top = 10.dp, bottom = 10.dp)
                                        .width(65.dp)
                                        .height(40.dp),
                                    onClick = { navController?.navigate(studentId) }) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(20.dp)
                                    )
                                }
                            }

                        }
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp)
                        ){
                            Text(modifier = Modifier
                                .fillMaxWidth(),
                                text = "${product.name}")
                            Text(modifier = Modifier
                                .fillMaxWidth(),
                                text = "${product.price}")
                        }

                    }
                }
            }
        }
    )
}

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun StudentListPreview() {
    MyApplicationTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            ProductList(navController = null)
        }
    }
}