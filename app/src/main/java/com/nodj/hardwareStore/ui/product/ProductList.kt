package com.nodj.hardwareStore.ui.product

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
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
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.nodj.hardwareStore.R
import com.nodj.hardwareStore.db.database.AppDatabase
import com.nodj.hardwareStore.db.database.Converters
import com.nodj.hardwareStore.db.models.Product
import com.nodj.hardwareStore.db.models.helperModels.AdvancedProduct
import com.nodj.hardwareStore.ui.AppViewModelProvider
import com.nodj.hardwareStore.ui.MyApplicationTheme
import com.nodj.hardwareStore.ui.navigation.Screen
import com.nodj.hardwareStore.ui.product.list.ProductListViewModel
import com.nodj.hardwareStore.ui.product.productCarts.ProductForCatalog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductList(
    navController: NavController,
    viewModel: ProductListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val pagingProduct: LazyPagingItems<Product> = viewModel.call().collectAsLazyPagingItems()
    val productListCartUiState by viewModel.productListCartUiState.collectAsState()
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
            productList = pagingProduct,
            productCartList = productListCartUiState.productListCart,
            onClick = { id: Int ->
                val route = Screen.ProductEdit.route.replace("{id}", id.toString())
                navController.navigate(route)
            },
            onClickViewProduct = { id: Int ->
                val route = Screen.ProductView.route.replace("{id}", id.toString())
                navController.navigate(route)
            },
            onClickViewCart = { id: Int ->
                val route = Screen.CartId.route.replace("{id}", id.toString())
                navController.navigate(route)
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
    onClickViewCart: (id: Int) -> Unit,
    onClickBuyProduct: (id: Int) -> Unit
) {
    SwipeToDismiss(
        state = dismissState,
        directions = setOf(
            DismissDirection.EndToStart
        ),
        background = {
            val iconScale by animateFloatAsState(
                targetValue = if (dismissState.targetValue == DismissValue.DismissedToStart) 1.3f else 0.0f,
                label = ""
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(end = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    modifier = Modifier
                        .scale(iconScale),
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete",
                    tint = Color.White
                )
            }
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
fun ProductList(
    modifier: Modifier = Modifier,
    productList: LazyPagingItems<Product>,
    productCartList: List<Product>,
    onClick: (id: Int) -> Unit,
    onClickViewProduct: (id: Int) -> Unit,
    onSwipe: (product: Product) -> Unit,
    onClickBuyProduct: (id: Int) -> Unit,
    onClickViewCart: (id: Int) -> Unit,
) {
        Column(
            modifier = modifier
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
                    items(productList.itemCount) { index ->
                        var inCart = false
                        val product = productList[index]
                        if (product != null){
                            val dismissState: DismissState = rememberDismissState(
                                positionalThreshold = { 200.dp.toPx() }
                            )
                            if (dismissState.isDismissed(direction = DismissDirection.EndToStart)) {
                                onSwipe(product)
                            }
                            if (Product.contains(productList, product.id)){
                                inCart = true
                            }
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
                    }
                }
            )
        }
}

@Composable
private fun ProductListItem(
    inCart: Boolean,
    product: Product,
    modifier: Modifier = Modifier,
    onClickViewProduct: (id: Int) -> Unit,
    onClickViewCart: (id: Int) -> Unit,
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
            bitmap = product.image.asImageBitmap(),
            contentDescription = "Продукт"
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ){
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(all = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                if(inCart){
                    Button(
                        modifier = Modifier
                            .width(130.dp)
                            .height(40.dp),
                        onClick = { onClickViewCart(1) }) {
                        Text(stringResource(R.string.go_to_cart))
                    }
                } else{
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
                ){
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Home Icon",
                        modifier = modifier
                        )
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