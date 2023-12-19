package com.nodj.hardwareStore.ui.page.cart


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.nodj.hardwareStore.LiveStore
import com.nodj.hardwareStore.R
import com.nodj.hardwareStore.common.MyViewModel
import com.nodj.hardwareStore.db.models.Order
import com.nodj.hardwareStore.db.models.helperModels.AdvancedProduct
import com.nodj.hardwareStore.db.models.manyToMany.UserWithProducts
import com.nodj.hardwareStore.db.repository.repositoryInterface.OrderRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.ProductRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.UserWithProductsRepository
import java.util.Date

class ProductListInCartViewModel(
    private val productRepository: ProductRepository,
    private val userWithProductsRepository: UserWithProductsRepository,
    private val orderRepository: OrderRepository,

    ) : MyViewModel() {

    var productListCartUiState by mutableStateOf(ProductListCartUiState())
        private set

    var error by mutableStateOf(0)
        private set

    fun clearError() {
        error = 0
    }

    fun update() {
        runInScope(
            actionSuccess = {
                productListCartUiState =
                    ProductListCartUiState(productRepository.getAllByUser(LiveStore.getUserId()))
            },
            actionError = {
                error = R.string.error_404
            }
        )
    }

    suspend fun deleteProductInCart(userId: Int, productid: Int) {
        runInScope(
            actionSuccess = {
                userWithProductsRepository.delete(UserWithProducts(userId, productid))
                update()
            },
            actionError = {
                error = R.string.error_404
            }
        )
    }

    suspend fun plusProductInCart(userId: Int, productid: Int) {
        runInScope(
            actionSuccess = {
                val product = userWithProductsRepository.getByUserProduct(productid, userId)
                if (product != null) {
                    userWithProductsRepository.update(
                        UserWithProducts(
                            userId,
                            productid,
                            product.count + 1
                        )
                    )
                    update()
                }
            },
            actionError = {
                error = R.string.error_404
            }
        )
    }

    suspend fun minusProductInCart(userId: Int, productid: Int) {
        runInScope(
            actionSuccess = {
                val product = userWithProductsRepository.getByUserProduct(productid, userId)
                if (product != null) {
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
            },
            actionError = {
                error = R.string.error_404
            }
        )
    }

    suspend fun createOrder(userId: Int) {
        runInScope(
            actionSuccess = {
                orderRepository.insert(Order(date = Date(), userId = userId))
                update()
            },
            actionError = {
                error = R.string.error_404
            }
        )

    }
}

data class ProductListCartUiState(val productListCart: List<AdvancedProduct> = listOf())