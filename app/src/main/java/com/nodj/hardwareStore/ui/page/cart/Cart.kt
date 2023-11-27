package com.nodj.hardwareStore.ui.page.cart

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nodj.hardwareStore.common.AppViewModelProvider
import com.nodj.hardwareStore.db.models.helperModels.AdvancedProduct
import com.nodj.hardwareStore.ui.MyApplicationTheme
import com.nodj.hardwareStore.ui.navigation.Screen
import com.nodj.hardwareStore.ui.product.productCarts.ProductForCart
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Cart(
    navController: NavController,
    viewModel: ProductListInCartViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val productListCartUiState by viewModel.productListCartUiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycle = lifecycleOwner.lifecycle
    val currentState by rememberUpdatedState(lifecycle.currentState)

    LaunchedEffect(currentState) {
        if (currentState == Lifecycle.State.RESUMED) {
            viewModel.update() // Update ViewModel when the page is resumed
        }
    }
//    val productListCartUiState = viewModel.productListCartUiState.collectAsLazyPagingItems()
    Cart(
//        update = { viewModel.update() },
        productListCart = productListCartUiState.productListCart,
//        productListCart = productListCartUiState,
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun Cart(
//    update: () -> Unit,
//    productListCart: LazyPagingItems<AdvancedProduct>,
    productListCart: List<AdvancedProduct>,
    onClickPlus: (id: Int) -> Unit,
    onClickMinus: (id: Int) -> Unit,
    onClickDelete: (id: Int) -> Unit,
    onClickViewProduct: (id: Int) -> Unit,
    onClickBuy: (id: Int) -> Unit,
) {
    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }
    fun refresh() = refreshScope.launch {
        refreshing = true
//        update()
        refreshing = false
    }

    val state = rememberPullRefreshState(refreshing, ::refresh)
//    val list = mutableListOf<AdvancedProduct>()
//    for (i in 0..productListCart.itemCount - 1) {
//        productListCart.get(i)?.let { list.add(it) }
//    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(state)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            LazyColumn(modifier = Modifier.padding(all = 10.dp)) {
                items(
                    count = productListCart.size,
//                    count = productListCart.itemCount,
//                    key = productListCart.itemKey(),
//                    contentType = productListCart.itemContentType()
                ) { index ->
                    productListCart[index]?.let {
                        ProductForCart(
                            advancedProduct = it,
                            onClickPlus = onClickPlus,
                            onClickMinus = onClickMinus,
                            onClickDelete = onClickDelete,
                            onClickViewProduct = onClickViewProduct,
                        )
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
                            Text(
                                "Цена: ${productListCart.sumOf { it.count * it.product.price }}",
//                                "Цена: ${list.sumOf { it.count * it.product.price }}",
                                textAlign = TextAlign.Start
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = CenterHorizontally
                            ) {
                                Button(
                                    modifier = Modifier
                                        .padding(top = 20.dp)
                                        .width(100.dp)
                                        .height(40.dp)
                                        .align(CenterHorizontally),
                                    onClick = { onClickBuy(1) }) {
                                    Text("Купить")
                                }
                            }
                        }
                    }
                }
            }
            PullRefreshIndicator(
                refreshing, state,
                Modifier
                    .align(CenterHorizontally)
                    .zIndex(100f)
            )
        }
    }
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