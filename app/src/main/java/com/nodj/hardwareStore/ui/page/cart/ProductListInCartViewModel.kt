package com.nodj.hardwareStore.ui.page.cart


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nodj.hardwareStore.LiveStore
import com.nodj.hardwareStore.db.models.Order
import com.nodj.hardwareStore.db.models.helperModels.AdvancedProduct
import com.nodj.hardwareStore.db.models.manyToMany.UserWithProducts
import com.nodj.hardwareStore.db.repository.repositoryInterface.OrderRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.OrderWithProductsRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.ProductRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.UserWithProductsRepository
import kotlinx.coroutines.launch
import java.util.Date

class ProductListInCartViewModel(
    private val productRepository: ProductRepository,
    private val userWithProductsRepository: UserWithProductsRepository,
    private val orderWithProductsRepository: OrderWithProductsRepository,
    private val orderRepository: OrderRepository,

    ) : ViewModel() {

    var productListCartUiState by mutableStateOf(ProductListCartUiState())
        private set

    fun update() {
        viewModelScope.launch {
            productListCartUiState =
                ProductListCartUiState(productRepository.getAllByUser(LiveStore.getUserId()))
        }
    }

    suspend fun deleteProductInCart(userId: Int, productid: Int) {
        userWithProductsRepository.delete(UserWithProducts(userId, productid))
        update()
    }

    suspend fun plusProductInCart(userId: Int, productid: Int) {
        val product = userWithProductsRepository.getByUserProduct(productid, userId)
        userWithProductsRepository.update(UserWithProducts(userId, productid, product.count + 1))
        update()
    }

    suspend fun minusProductInCart(userId: Int, productid: Int) {
        val product = userWithProductsRepository.getByUserProduct(productid, userId)
        if (product.count - 1 <= 0) {
            deleteProductInCart(userId, productid)
        } else {
            userWithProductsRepository.update(
                UserWithProducts(
                    userId,
                    productid,
                    product.count - 1
                )
            )
        }
        update()
    }

    suspend fun createOrder(userId: Int) {
        orderRepository.insert(Order(date = Date(), userId = userId))
        update()
    }
}

data class ProductListCartUiState(val productListCart: List<AdvancedProduct> = listOf())