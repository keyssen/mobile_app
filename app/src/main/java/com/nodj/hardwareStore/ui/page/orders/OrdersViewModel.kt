package com.nodj.hardwareStore.ui.page.orders


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.nodj.hardwareStore.LiveStore
import com.nodj.hardwareStore.R
import com.nodj.hardwareStore.common.MyViewModel
import com.nodj.hardwareStore.db.models.Order
import com.nodj.hardwareStore.db.models.helperModels.ProductFromOrder
import com.nodj.hardwareStore.db.repository.repositoryInterface.OrderRepository

class OrdersViewModel(
    private val orderRepository: OrderRepository
) : MyViewModel() {

    var orderListUiState by mutableStateOf(OrderListUiState())
        private set

    var error by mutableStateOf(0)
        private set

    fun clearError() {
        error = 0
    }

    fun update() {
        runInScope(
            actionSuccess = {
                orderListUiState =
                    OrderListUiState(orderRepository.getAllByUser(LiveStore.getUserId()))
            },
            actionError = {
                error = R.string.error_404
            }
        )
    }
}

data class OrderListUiState(val orderList: Map<Order, List<ProductFromOrder>> = mapOf())