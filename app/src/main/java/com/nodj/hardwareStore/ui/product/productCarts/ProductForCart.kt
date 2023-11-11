package com.nodj.hardwareStore.ui.product.productCarts

import android.content.Context
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.nodj.hardwareStore.db.database.AppDatabase
import com.nodj.hardwareStore.db.models.helperModels.AdvancedProduct
import com.nodj.hardwareStore.db.models.manyToMany.UserWithProducts
import com.nodj.hardwareStore.ui.MyApplicationTheme
import com.nodj.hardwareStore.ui.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductForCart(context: Context, navController: NavController?, productInCart: AdvancedProduct) {
    val productId = Screen.ProductView.route.replace("{id}", productInCart.product.id.toString())
    var count by remember { mutableStateOf(productInCart.count) }
    val coroutineScope = rememberCoroutineScope()
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
                IconButton(onClick = {
                        coroutineScope.launch {
                            updateUserWithProducts(context, productInCart.product.id, 1 , productInCart.count + 1)
                        }
                                     },
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .size(20.dp)) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = null)
                }
                Text(text = count.toString(),
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .size(20.dp))
                IconButton(onClick = { navController?.navigate(productId) },
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .size(20.dp)) {
                    Icon(
                        ImageVector.vectorResource(id = R.drawable.minus),
                        contentDescription = null)
                }
                IconButton(onClick = { navController?.navigate(productId) },
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .size(20.dp)) {
                    Icon(
                        Icons.Filled.Delete,
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

suspend fun updateUserWithProducts(context: Context, productId: Int?, UserId: Int, count: Int) {
    val userWithProducts = UserWithProducts(UserId.toLong(), productId!!.toLong(), count)
    AppDatabase.getInstance(context).userWithProductsDao().update(userWithProducts)
}

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProductForCartPreview() {
    MyApplicationTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            val context = LocalContext.current
            val productInCart = remember { mutableStateOf<AdvancedProduct>(AdvancedProduct.getEmpty()) }
            LaunchedEffect(Unit) {
                withContext(Dispatchers.IO) {
                    productInCart.value = AppDatabase.getInstance(context).productDao().get(1, 1)
                }
            }
            ProductForCart(context, navController = null, productInCart.value)
        }
    }
}
