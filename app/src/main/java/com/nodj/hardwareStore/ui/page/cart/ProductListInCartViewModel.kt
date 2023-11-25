package com.nodj.hardwareStore.ui.page.cart


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nodj.hardwareStore.db.database.AppDataContainer
import com.nodj.hardwareStore.db.models.Order
import com.nodj.hardwareStore.db.models.Product
import com.nodj.hardwareStore.db.models.helperModels.AdvancedProduct
import com.nodj.hardwareStore.db.models.manyToMany.UserWithProducts
import com.nodj.hardwareStore.db.repository.repositoryInterface.OrderRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.ProductRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.UserWithProductsRepository
import com.nodj.hardwareStore.db.service.OrderService
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Date

class ProductListInCartViewModel(
    private val productRepository: ProductRepository,
    private val userWithProductsRepository: UserWithProductsRepository,
    private val orderService: OrderService
) : ViewModel() {

    val productListCartUiState: StateFlow<ProductListCartUiState> = productRepository.getAllByUserFlow(1).map {
        ProductListCartUiState(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = AppDataContainer.TIMEOUT),
        initialValue = ProductListCartUiState()
    )

    suspend fun deleteProductInCart(userId: Int, productid: Int) {
        userWithProductsRepository.delete(UserWithProducts(userId, productid))
    }

    suspend fun plusProductInCart(userId: Int, productid: Int, ) {
        val product = userWithProductsRepository.getByUserProduct(productid, userId)
        userWithProductsRepository.update(UserWithProducts(userId, productid, product.count + 1))
    }

    suspend fun minusProductInCart(userId: Int, productid: Int, ) {
        val product = userWithProductsRepository.getByUserProduct(productid, userId)
        if (product.count - 1 <= 0){
            deleteProductInCart(userId, productid)
        }else{
            userWithProductsRepository.update(UserWithProducts(userId, productid, product.count - 1))
        }
    }

    suspend fun createOrder(userId: Int) {
        orderService.createOrder(userId)
    }
}

data class ProductListCartUiState(val productListCart: List<AdvancedProduct> = listOf())