package com.nodj.myapplication.ui.theme.screen

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nodj.myapplication.R
import com.nodj.myapplication.ui.theme.MyApplicationTheme
import com.nodj.myapplication.ui.theme.composeui.navigation.Screen
import com.nodj.myapplication.ui.theme.product.model.Product
import com.nodj.myapplication.ui.theme.product.model.getProducts

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Cart(navController: NavController?) {
    val productList: List<Product> = getProducts()
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        contentPadding = PaddingValues(
            start = 12.dp,
            top = 16.dp,
            end = 12.dp,
            bottom = 16.dp
        ),
        content = {
            items(productList.size) { index ->
                val studentId = Screen.ProductView.route.replace("{id}", index.toString())
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
                        bitmap = ImageBitmap.imageResource(R.drawable.phone),
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
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp)
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                text = "${productList[index].name}"
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                text = "${productList[index].price}"
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