package com.nodj.hardwareStore.db.theme.order

import android.content.res.Configuration
import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nodj.hardwareStore.common.AppViewModelProvider
import com.nodj.hardwareStore.db.models.Order
import com.nodj.hardwareStore.db.models.helperModels.ProductFromOrder
import com.nodj.hardwareStore.ui.MyApplicationTheme
import com.nodj.hardwareStore.ui.UI.showToast
import com.nodj.hardwareStore.ui.navigation.Screen
import com.nodj.hardwareStore.ui.page.orders.OrdersViewModel
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderList(
    navController: NavController,
    viewModel: OrdersViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val orderListUiState = viewModel.orderListUiState
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.update()
    }
    if (viewModel.error != 0) {
        showToast(context, stringResource(viewModel.error))
        viewModel.clearError()
    }
    OrderList(
        update = { viewModel.update() },
        orderList = orderListUiState.orderList.toList(),
        onClickViewOrder = { id: Int ->
            val route = Screen.Order.route.replace("{id}", id.toString())
            navController.navigate(route)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun OrderList(
    update: () -> Unit,
    orderList: List<Pair<Order, List<ProductFromOrder>>>,
    onClickViewOrder: (id: Int) -> Unit,
) {
    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }
    fun refresh() = refreshScope.launch {
        refreshing = true
        update()
        refreshing = false
    }

    val state = rememberPullRefreshState(refreshing, ::refresh)
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
                items(orderList.size) { index ->
                    val order = orderList[index].first
                    val listProduct = orderList[index].second
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(125.dp)
                            .padding(top = 10.dp)
                            .border(2.dp, Color.Gray, RoundedCornerShape(10.dp))
                            .clickable {
                                if (order != null) {
                                    onClickViewOrder(order.id)
                                }
                            },
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp, start = 10.dp),
                                text = "ID: ${order?.id}"
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp, start = 10.dp),
                                text = "Дата: ${order?.let { convertDate(it.date) }}"
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp, start = 10.dp),
                                text = "Цена: ${listProduct?.sumOf { it.count * it.currentPrice }}"
                            )
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
fun OrderListPreview() {
    MyApplicationTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
//            OrderList(navController = null)
        }
    }
}

fun convertDate(inputDate: Date): String {
    val outputFormat = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale("ru", "RU"))
    return outputFormat.format(inputDate)
}
