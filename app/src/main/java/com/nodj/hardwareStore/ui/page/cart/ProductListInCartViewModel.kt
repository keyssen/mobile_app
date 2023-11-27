package com.nodj.hardwareStore.ui.page.cart


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nodj.hardwareStore.common.AppContainer
import com.nodj.hardwareStore.db.models.Order
import com.nodj.hardwareStore.db.models.helperModels.AdvancedProduct
import com.nodj.hardwareStore.db.models.manyToMany.OrderWithProducts
import com.nodj.hardwareStore.db.models.manyToMany.UserWithProducts
import com.nodj.hardwareStore.db.repository.repositoryInterface.IncompleteProductRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.OrderRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.OrderWithProductsRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.UserWithProductsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date

class ProductListInCartViewModel(
    private val productRepository: IncompleteProductRepository,
    private val userWithProductsRepository: UserWithProductsRepository,
    private val orderWithProductsRepository: OrderWithProductsRepository,
    private val orderRepository: OrderRepository,

    ) : ViewModel() {

//    val productListCartUiState: Flow<PagingData<AdvancedProduct>> = productRepository.getByUser(1)

    val productListCartUiState: StateFlow<ProductListCartUiState> =
        productRepository.getAllByUserFlow(1).map {
            ProductListCartUiState(it)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = AppContainer.TIMEOUT),
            initialValue = ProductListCartUiState()
        )

    init {
        update()
    }

    fun update() {
        viewModelScope.launch {
            userWithProductsRepository.getAll()
        }
    }

    suspend fun deleteProductInCart(userId: Int, productid: Int) {
        userWithProductsRepository.delete(UserWithProducts(userId, productid))
        /*        update()*/
    }

    suspend fun plusProductInCart(userId: Int, productid: Int) {
        val product = userWithProductsRepository.getByUserProduct(productid, userId)
        userWithProductsRepository.update(UserWithProducts(userId, productid, product.count + 1))
        /* update()*/
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
        /*update()*/
    }

    suspend fun createOrder(userId: Int) {
        var order = orderRepository.insert(Order(date = Date(), userId = userId))
        if (order.toInt() != 0) {
            productRepository.getAllByUser(userId).forEach() {
                orderWithProductsRepository.insert(
                    OrderWithProducts(
                        orderId = order,
                        productId = it.product.id.toLong(),
                        count = it.count,
                        currentPrice = it.product.price
                    )
                )
            }
            userWithProductsRepository.deleteAllByUser(userId)
        }
    }

//    suspend fun createOrder(userId: Int) {
//        orderService.createOrder(userId)
//    }
}

data class ProductListCartUiState(val productListCart: List<AdvancedProduct> = listOf())