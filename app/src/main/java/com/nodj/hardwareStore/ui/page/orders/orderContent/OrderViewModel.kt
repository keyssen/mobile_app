package com.nodj.hardwareStore.ui.page.orders.orderContent


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.nodj.hardwareStore.LiveStore
import com.nodj.hardwareStore.R
import com.nodj.hardwareStore.common.MyViewModel
import com.nodj.hardwareStore.db.models.Product
import com.nodj.hardwareStore.db.models.helperModels.ProductFromOrder
import com.nodj.hardwareStore.db.models.manyToMany.UserWithProducts
import com.nodj.hardwareStore.db.repository.repositoryInterface.OrderRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.ProductRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.UserWithProductsRepository
import kotlinx.coroutines.launch

class OrderViewModel(
    savedStateHandle: SavedStateHandle,
    private val productRepository: ProductRepository,
    private val orderRepository: OrderRepository,
    private val userWithProductsRepository: UserWithProductsRepository
) : MyViewModel() {

    private val orderId: Int = checkNotNull(savedStateHandle["id"])

    var orderUiState by mutableStateOf(OrderUiState())
        private set

    var error by mutableStateOf(0)
        private set

    fun clearError() {
        error = 0
    }

    var productListCart by mutableStateOf(ProductListCart())
        private set

    fun update() {
        viewModelScope.launch {
            if (orderId > 0) {
                runInScope(
                    actionSuccess = {
                        orderUiState = OrderUiState(orderRepository.getByOrder(orderId))
                        productListCart =
                            ProductListCart(productRepository.getAllByUserProduct(LiveStore.getUserId()))
                    },
                    actionError = {
                        error = R.string.error_404
                    }
                )
            }
        }
    }

    suspend fun addToCartProduct(productid: Int) {
        runInScope(
            actionSuccess = {
                userWithProductsRepository.insert(
                    UserWithProducts(
                        LiveStore.getUserId(),
                        productid,
                        1
                    )
                )
                update()
            },
            actionError = {
                error = R.string.error_404
            }
        )
    }
}

data class OrderUiState(val productList: List<ProductFromOrder> = listOf())

data class ProductListCart(val productListCart: List<Product> = listOf())