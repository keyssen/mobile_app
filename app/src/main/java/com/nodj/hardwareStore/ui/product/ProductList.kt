package com.nodj.hardwareStore.ui.product

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.nodj.hardwareStore.R
import com.nodj.hardwareStore.common.AppViewModelProvider
import com.nodj.hardwareStore.db.models.Product
import com.nodj.hardwareStore.ui.MyApplicationTheme
import com.nodj.hardwareStore.ui.navigation.Screen
import com.nodj.hardwareStore.ui.navigation.changeLocationDeprecated
import com.nodj.hardwareStore.ui.product.list.ProductListViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ProductList(
    navController: NavController,
    viewModel: ProductListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val productListUiState = viewModel.productListUiState.collectAsLazyPagingItems()
    val productListCartUiState = viewModel.productListCartUiState
    LaunchedEffect(Unit) {
        viewModel.update()
    }
    Scaffold(
        topBar = {},
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val route = Screen.ProductEdit.route.replace("{id}", 0.toString())
                    navController.navigate(route)
                },
            ) {
                Icon(Icons.Filled.Add, "Добавить")
            }
        }
    ) { innerPadding ->
        ProductList(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            productList = productListUiState,
            productCartList = productListCartUiState.productListCart,
            onClick = { id: Int ->
                val route = Screen.ProductEdit.route.replace("{id}", id.toString())
                navController.navigate(route)
            },
            onClickViewProduct = { id: Int ->
                val route = Screen.ProductView.route.replace("{id}", id.toString())
                navController.navigate(route)
            },
            onClickViewCart = {
                val route = Screen.Cart.route
                changeLocationDeprecated(navController, route)
            },
            onClickBuyProduct = { id: Int ->
                coroutineScope.launch {
                    viewModel.addToCartProduct(id)
                }
            },
            onSwipe = { product: Product ->
                coroutineScope.launch {
                    viewModel.deleteProduct(product)
                }
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeToDelete(
    dismissState: DismissState,
    inCart: Boolean,
    product: Product,
    onClick: (id: Int) -> Unit,
    onClickViewProduct: (id: Int) -> Unit,
    onClickViewCart: () -> Unit,
    onClickBuyProduct: (id: Int) -> Unit
) {
    SwipeToDismiss(
        modifier = Modifier.zIndex(1f),
        state = dismissState,
        directions = setOf(
            DismissDirection.EndToStart
        ),
        background = {
            DismissBackground(dismissState)
        },
        dismissContent = {
            ProductListItem(
                inCart = inCart,
                product = product,
                onClickViewProduct = onClickViewProduct,
                onClickViewCart = onClickViewCart,
                onClickBuyProduct = onClickBuyProduct,
                modifier = Modifier
                    .clickable { onClick(product.id) }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackground(dismissState: DismissState) {
    val color = when (dismissState.dismissDirection) {
        DismissDirection.StartToEnd -> Color.Transparent
        DismissDirection.EndToStart -> Color(0xFFFF1744)
        null -> Color.Transparent
    }
    val direction = dismissState.dismissDirection

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(12.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        if (direction == DismissDirection.EndToStart) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "delete",
                tint = Color.White
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ProductList(
    modifier: Modifier = Modifier,
    productList: LazyPagingItems<Product>,
    productCartList: List<Product>,
    onClick: (id: Int) -> Unit,
    onClickViewProduct: (id: Int) -> Unit,
    onSwipe: (product: Product) -> Unit,
    onClickBuyProduct: (id: Int) -> Unit,
    onClickViewCart: () -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        LazyColumn(modifier = Modifier.padding(all = 10.dp)) {
            items(
                count = productList.itemCount,
                key = productList.itemKey(),
                contentType = productList.itemContentType()
            ) { index ->
                var inCart = false
                val product = productList[index]
                if (product != null) {
                    var show by remember { mutableStateOf(true) }
                    val dismissState = rememberDismissState(
                        confirmValueChange = {
                            if (it == DismissValue.DismissedToStart ||
                                it == DismissValue.DismissedToEnd
                            ) {
                                show = false
                                true
                            } else false
                        }, positionalThreshold = { 200.dp.toPx() }
                    )
                    if (Product.contains(productCartList, product.id)) {
                        inCart = true
                    }
                    AnimatedVisibility(
                        show, exit = fadeOut(spring())
                    ) {
                        SwipeToDelete(
                            dismissState = dismissState,
                            product = product,
                            inCart = inCart,
                            onClick = onClick,
                            onClickViewProduct = onClickViewProduct,
                            onClickBuyProduct = onClickBuyProduct,
                            onClickViewCart = onClickViewCart
                        )
                    }
                    LaunchedEffect(show) {
                        if (!show) {
                            delay(800)
                            onSwipe(product)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductListItem(
    inCart: Boolean,
    product: Product,
    modifier: Modifier = Modifier,
    onClickViewProduct: (id: Int) -> Unit,
    onClickViewCart: () -> Unit,
    onClickBuyProduct: (id: Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(top = 10.dp)
            .border(2.dp, Color.Gray, RoundedCornerShape(10.dp))
            .clickable {
                onClickViewProduct(product.id)
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
                    .fillMaxWidth()
                    .padding(all = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (inCart) {
                    Button(
                        modifier = Modifier
                            .width(130.dp)
                            .height(40.dp),
                        onClick = { onClickViewCart() }) {
                        Text(stringResource(R.string.go_to_cart))
                    }
                } else {
                    Button(
                        modifier = Modifier
                            .width(100.dp)
                            .height(40.dp),
                        onClick = { onClickBuyProduct(product.id) }) {
                        Text(stringResource(R.string.product_buy))
                    }
                }
                Box(
                    modifier = Modifier
                        .padding(top = 7.dp, end = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Home Icon",
                        modifier = modifier
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
fun ProductListPreview() {
    MyApplicationTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
//            ProductList(
//                productList = lazyOf(),
//                productCartList = listOf(),
//                onClick = {},
//                onClickViewProduct = {},
//                onClickViewCart = {},
//                onSwipe = {},
//                onClickBuyProduct = {}
//            )
        }
    }
}