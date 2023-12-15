package com.nodj.hardwareStore.ui.page.orders


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nodj.hardwareStore.LiveStore
import com.nodj.hardwareStore.db.models.Order
import com.nodj.hardwareStore.db.models.helperModels.ProductFromOrder
import com.nodj.hardwareStore.db.repository.repositoryInterface.OrderRepository
import kotlinx.coroutines.launch

class OrdersViewModel(
    private val orderRepository: OrderRepository
) : ViewModel() {

    var orderListUiState by mutableStateOf(OrderListUiState())
        private set

    fun update() {
        viewModelScope.launch {
            orderListUiState = OrderListUiState(orderRepository.getAllByUser(LiveStore.getUserId()))
        }
    }
}

data class OrderListUiState(val orderList: Map<Order, List<ProductFromOrder>> = mapOf())