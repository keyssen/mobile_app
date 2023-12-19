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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nodj.hardwareStore.LiveStore
import com.nodj.hardwareStore.common.AppViewModelProvider
import com.nodj.hardwareStore.db.models.helperModels.AdvancedProduct
import com.nodj.hardwareStore.ui.MyApplicationTheme
import com.nodj.hardwareStore.ui.UI.showToast
import com.nodj.hardwareStore.ui.navigation.Screen
import com.nodj.hardwareStore.ui.page.product.productCarts.ProductForCart
import kotlinx.coroutines.launch
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Cart(
    navController: NavController,
    viewModel: ProductListInCartViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val productListCartUiState = viewModel.productListCartUiState
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.update()
    }
    if (viewModel.error != 0) {
        showToast(context, stringResource(viewModel.error))
        viewModel.clearError()
    }
    Cart(
        productListCart = productListCartUiState.productListCart,
        onClickPlus = { id: Int ->
            coroutineScope.launch {
                viewModel.plusProductInCart(LiveStore.getUserId(), id)
            }
        },
        onClickMinus = { id: Int ->
            coroutineScope.launch {
                viewModel.minusProductInCart(LiveStore.getUserId(), id)
            }
        },
        onClickDelete = { id: Int ->
            coroutineScope.launch {
                viewModel.deleteProductInCart(LiveStore.getUserId(), id)
            }
        },
        onClickViewProduct = { id: Int ->
            val route = Screen.Product.route.replace("{id}", id.toString())
            navController.navigate(route)
        },
        onClickBuy = {
            coroutineScope.launch {
                viewModel.createOrder(LiveStore.getUserId())
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun Cart(
    productListCart: List<AdvancedProduct>,
    onClickPlus: (id: Int) -> Unit,
    onClickMinus: (id: Int) -> Unit,
    onClickDelete: (id: Int) -> Unit,
    onClickViewProduct: (id: Int) -> Unit,
    onClickBuy: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        LazyColumn(modifier = Modifier.padding(all = 10.dp)) {
            items(
                count = productListCart.size,
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
                            "Цена: ${
                                String.format(
                                    Locale.US,
                                    "%.2f",
                                    productListCart.sumOf { it.count * it.product.price }
                                )
                            }",
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
                                onClick = { onClickBuy() }) {
                                Text("Купить")
                            }
                        }
                    }
                }
            }
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
        }
    }
}