package com.nodj.hardwareStore.ui.page.product.productCarts

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nodj.hardwareStore.LiveStore
import com.nodj.hardwareStore.db.models.Product
import com.nodj.hardwareStore.ui.MyApplicationTheme
import com.nodj.hardwareStore.ui.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductForCatalog(
    navController: NavController?,
    inTheBasket: Boolean = true,
    product: Product
) {
    val productId = Screen.Product.route.replace("{id}", product.id.toString())
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(top = 10.dp)
            .border(2.dp, Color.Gray, RoundedCornerShape(10.dp))
            .clickable {
                navController?.navigate(productId)
            },
    ) {
        Image(
            modifier = Modifier
                .width(110.dp)
                .height(160.dp)
                .padding(start = 10.dp),
            bitmap = Product.toBitmap(product.image).asImageBitmap(),
            contentDescription = "Продукт"
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (LiveStore.getUserId() != 0) {
                    if (!inTheBasket) {
                        Button(
                            modifier = Modifier
                                .padding(all = 10.dp)
                                .width(100.dp)
                                .height(40.dp),
                            onClick = { navController?.navigate(productId) }) {
                            Text("Купить")
                        }
                    } else {
                        Button(
                            modifier = Modifier
                                .padding(all = 10.dp)
                                .width(175.dp)
                                .height(40.dp),
                            onClick = { navController?.navigate(productId) }) {
                            Text("Перейти в корзину")
                        }
                    }
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
                    text = "${product.name}"
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "${product.price}"
                )
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProductForCatalogPreview() {
    MyApplicationTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            val context = LocalContext.current
            val product = remember { mutableStateOf<Product>(Product.getEmpty()) }
            LaunchedEffect(Unit) {
                withContext(Dispatchers.IO) {
//                    product.value = AppDatabase.getInstance(context).productDao().getProduct(1)
                }
            }
            ProductForCatalog(navController = null, false, product.value)
        }
    }
}
