package com.nodj.hardwareStore.ui.page.orders.order


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nodj.hardwareStore.db.database.AppDataContainer
import com.nodj.hardwareStore.db.models.Order
import com.nodj.hardwareStore.db.models.Product
import com.nodj.hardwareStore.db.models.helperModels.AdvancedProduct
import com.nodj.hardwareStore.db.models.helperModels.ProductFromOrder
import com.nodj.hardwareStore.db.models.manyToMany.UserWithProducts
import com.nodj.hardwareStore.db.repository.repositoryInterface.OrderRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.ProductRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.UserWithProductsRepository
import com.nodj.hardwareStore.db.service.OrderService
import com.nodj.hardwareStore.ui.product.edit.ProductDetails
import com.nodj.hardwareStore.ui.product.edit.ProductUiState
import com.nodj.hardwareStore.ui.product.edit.toUiState
import com.nodj.hardwareStore.ui.product.list.ProductListCartUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date

class OrderViewModel(
    savedStateHandle: SavedStateHandle,
    private val productRepository: ProductRepository,
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
                 orderRepository.getByOrder(orderId).collect(){
                    orderUiState = OrderUiState(it)
                     productRepository.getAllByUserProduct(1).collect {
                         productListCartUiState = ProductListCartUiState(it)
                     }
                }
            }
        }
    }

    suspend fun addToCartProduct(productid: Int) {
        userWithProductsRepository.insert(UserWithProducts(1, productid, 1))
    }
}

data class OrderUiState(val productList: List<ProductFromOrder> = listOf())

data class ProductListCartUiState(val productListCart: List<Product> = listOf())