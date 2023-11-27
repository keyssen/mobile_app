package com.nodj.hardwareStore.ui.page.orders.order


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nodj.hardwareStore.db.models.Product
import com.nodj.hardwareStore.db.models.helperModels.ProductFromOrder
import com.nodj.hardwareStore.db.models.manyToMany.UserWithProducts
import com.nodj.hardwareStore.db.repository.repositoryInterface.IncompleteProductRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.OrderRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.UserWithProductsRepository
import com.nodj.hardwareStore.ui.product.list.ProductListCartUiState
import kotlinx.coroutines.launch

class OrderViewModel(
    savedStateHandle: SavedStateHandle,
    private val productRepository: IncompleteProductRepository,
    private val orderRepository: OrderRepository,
    private val userWithProductsRepository: UserWithProductsRepository
) : ViewModel() {

    private val orderId: Int = checkNotNull(savedStateHandle["id"])

    var orderUiState by mutableStateOf(OrderUiState())
        private set

    var productListCartUiState by mutableStateOf(ProductListCartUiState())
        private set

    init {
        viewModelScope.launch {
            if (orderId > 0) {
                orderUiState = OrderUiState(orderRepository.getByOrder(orderId))
                productListCartUiState =
                    ProductListCartUiState(productRepository.getAllByUserProduct(1))
            }
        }
    }

    suspend fun addToCartProduct(productid: Int) {
        userWithProductsRepository.insert(UserWithProducts(1, productid, 1))
    }
}

data class OrderUiState(val productList: List<ProductFromOrder> = listOf())

data class ProductListCartUiState(val productListCart: List<Product> = listOf())