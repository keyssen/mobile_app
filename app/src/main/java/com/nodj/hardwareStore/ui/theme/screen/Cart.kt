package com.nodj.hardwareStore.ui.theme.screen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nodj.hardwareStore.R
import com.nodj.hardwareStore.ui.theme.MyApplicationTheme
import com.nodj.hardwareStore.ui.theme.database.AppDatabase
import com.nodj.hardwareStore.ui.theme.models.helperModels.AdvancedProduct
import com.nodj.hardwareStore.ui.theme.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Cart(navController: NavController?) {
    val context = LocalContext.current
    val productsInCart = remember { mutableStateListOf<AdvancedProduct>() }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            productsInCart.clear()
            productsInCart.addAll(AppDatabase.getInstance(context).productDao().getAllByUser(1))
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        contentPadding = PaddingValues(
            start = 12.dp,
            top = 16.dp,
            end = 12.dp,
            bottom = 16.dp
        ),
        content = {
            items(productsInCart.size) { index ->
                val studentId = Screen.ProductView.route.replace("{id}", index.toString())
                val productInCart = productsInCart[index]
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
                        bitmap = ImageBitmap.imageResource(productInCart.product.imageId),
                        contentDescription = "Продукт"
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp)
                                .padding(end = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            IconButton(onClick = { navController?.navigate(studentId) },
                                modifier = Modifier
                                    .padding(top = 20.dp)
                                    .size(20.dp)) {
                                Icon(Icons.Filled.Add,
                                    contentDescription = null)
                            }
                            Text(text = productInCart.count.toString(),
                                modifier = Modifier
                                    .padding(top = 20.dp)
                                    .size(20.dp))
                            IconButton(onClick = { navController?.navigate(studentId) },
                                modifier = Modifier
                                    .padding(top = 20.dp)
                                    .size(20.dp)) {
                                Icon(ImageVector.vectorResource(id = R.drawable.minus),
                                    contentDescription = null)
                            }
                            IconButton(onClick = { navController?.navigate(studentId) },
                                modifier = Modifier
                                    .padding(top = 20.dp)
                                    .size(20.dp)) {
                                Icon(Icons.Filled.Delete,
                                    contentDescription = null)
                            }
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp)
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                text = "${productInCart.product.name}"
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                text = "${productInCart.product.price}"
                            )
                        }

                    }
                }
            }
            item {
                Box(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .fillMaxWidth()
                        .background(Color.Gray)
                        .height(100.dp)
                        .padding(all = 10.dp),
                ) {
                    Column {
                        Text("Цена: 20000", textAlign = TextAlign.Start)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(
                                modifier = Modifier
                                    .padding(top = 20.dp)
                                    .width(100.dp)
                                    .height(40.dp)
                                    .align(Alignment.CenterHorizontally),
                                onClick = {}) {
                                Text("Купить")
                            }
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
fun CartPreview() {
    MyApplicationTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Cart(navController = null)
        }
    }
}