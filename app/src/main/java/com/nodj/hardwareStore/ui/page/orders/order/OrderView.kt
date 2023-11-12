package com.nodj.hardwareStore.db.theme.order

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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nodj.hardwareStore.R
import com.nodj.hardwareStore.db.database.AppDatabase
import com.nodj.hardwareStore.db.models.Product
import com.nodj.hardwareStore.db.models.helperModels.AdvancedProduct
import com.nodj.hardwareStore.db.models.helperModels.ProductFromOrder
import com.nodj.hardwareStore.ui.AppViewModelProvider
import com.nodj.hardwareStore.ui.MyApplicationTheme
import com.nodj.hardwareStore.ui.navigation.Screen
import com.nodj.hardwareStore.ui.page.orders.OrdersViewModel
import com.nodj.hardwareStore.ui.page.orders.order.OrderViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderView(
    navController: NavController,
    viewModel: OrderViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    OrderView(
        productList = viewModel.orderUiState.productList,
        productListCart = viewModel.productListCartUiState.productListCart,
        onClickViewProduct = { id: Int ->
            val route = Screen.ProductView.route.replace("{id}", id.toString())
            navController.navigate(route)
        },
        onClickBuyProduct = { id: Int ->
            coroutineScope.launch {
                viewModel.addToCartProduct(id)
            }
        },
        onClickViewCart = {
            val route = Screen.Cart.route
            navController.navigate(route)
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderView(
    productList: List<ProductFromOrder>,
    productListCart: List<Product>,
    onClickViewProduct: (id: Int) -> Unit,
    onClickBuyProduct: (id: Int) -> Unit,
    onClickViewCart: () -> Unit
) {
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
                val productFromOrder = productList[index]
                var inCart = false
                if (productListCart.contains(productFromOrder.product)){
                    inCart = true
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(top = 10.dp)
                        .border(2.dp, Color.Gray, RoundedCornerShape(10.dp))
                        .clickable {
                            onClickViewProduct(productFromOrder.product.id)
                        },
                ) {
                    Image(
                        modifier = Modifier
                            .width(110.dp)
                            .height(160.dp)
                            .padding(start = 10.dp),
                        bitmap = Product.toBitmap(productFromOrder.product.image).asImageBitmap(),
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
                            if(inCart){
                                Button(
                                    modifier = Modifier
                                        .padding(all = 10.dp)
                                        .width(130.dp)
                                        .height(40.dp),
                                    onClick = { onClickViewCart() }) {
                                    Text(stringResource(R.string.go_to_cart))
                                }
                            } else{
                                Button(
                                    modifier = Modifier
                                        .padding(all = 10.dp)
                                        .width(100.dp)
                                        .height(40.dp),
                                    onClick = { onClickBuyProduct(productFromOrder.product.id) }) {
                                    Text(stringResource(R.string.product_buy))
                                }
                            }
                            Text(text = productFromOrder.count.toString(),
                                modifier = Modifier
                                    .padding(top = 20.dp)
                                    .size(20.dp))
                        }
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp)
                        ){
                            Text(modifier = Modifier
                                .fillMaxWidth(),
                                text = "${productFromOrder.product.name}")
                            Text(modifier = Modifier
                                .fillMaxWidth(),
                                text = "${productFromOrder.product.price}")
                        }

                    }
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(top = 10.dp)
                        .border(2.dp, Color.Gray, RoundedCornerShape(10.dp)),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ){
                        Text(modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, start = 10.dp),
                            text = "Цена: ${productList.sumOf { it.count * it.currentPrice }}")
                    }
                }
            }
        }
    )
}

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OrderViewPreview() {
    MyApplicationTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
//            OrderView(navController = null, id = 0)
        }
    }
}