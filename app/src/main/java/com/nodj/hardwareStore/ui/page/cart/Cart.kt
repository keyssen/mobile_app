package com.nodj.hardwareStore.ui.page.cart

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nodj.hardwareStore.db.database.AppDatabase
import com.nodj.hardwareStore.db.models.helperModels.AdvancedProduct
import com.nodj.hardwareStore.ui.AppViewModelProvider
import com.nodj.hardwareStore.ui.MyApplicationTheme
import com.nodj.hardwareStore.ui.navigation.Screen
import com.nodj.hardwareStore.ui.product.productCarts.ProductForCart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Cart(
    navController: NavController,
    viewModel: ProductListInCartViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val productListCartUiState by viewModel.productListCartUiState.collectAsState()
    Cart(
        productListCart = productListCartUiState.productListCart,
        onClickPlus = { id: Int ->
            coroutineScope.launch {
                viewModel.plusProductInCart(1, id)
            }
        },
        onClickMinus = { id: Int ->
            coroutineScope.launch {
                viewModel.minusProductInCart(1, id)
            }
        },
        onClickDelete = { id: Int ->
            coroutineScope.launch {
                viewModel.deleteProductInCart(1, id)
            }
        },
        onClickViewProduct = { id: Int ->
            val route = Screen.ProductView.route.replace("{id}", id.toString())
            navController.navigate(route)
        },
        onClickBuy = { id: Int ->
            coroutineScope.launch {
                viewModel.createOrder(1)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Cart(
    productListCart: List<AdvancedProduct>,
    onClickPlus: (id: Int) -> Unit,
    onClickMinus: (id: Int) -> Unit,
    onClickDelete: (id: Int) -> Unit,
    onClickViewProduct: (id: Int) -> Unit,
    onClickBuy: (id: Int) -> Unit,
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
            items(productListCart.size) { index ->
                ProductForCart(
                    advancedProduct = productListCart[index],
                    onClickPlus = onClickPlus,
                    onClickMinus = onClickMinus,
                    onClickDelete = onClickDelete,
                    onClickViewProduct = onClickViewProduct,
                )
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
                        Text("Цена: ${productListCart.sumOf { it.count * it.product.price }}", textAlign = TextAlign.Start)
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
                                onClick = {onClickBuy(1)}) {
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
//            Cart(navController = null)
        }
    }
}