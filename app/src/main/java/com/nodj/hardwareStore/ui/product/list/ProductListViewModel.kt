package com.nodj.hardwareStore.ui.product.list


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.nodj.hardwareStore.common.AppContainer
import com.nodj.hardwareStore.db.models.Product
import com.nodj.hardwareStore.db.models.manyToMany.UserWithProducts
import com.nodj.hardwareStore.db.repository.repositoryInterface.IncompleteProductRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.UserWithProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ProductListViewModel(
    private val productRepository: IncompleteProductRepository,
    private val userWithProductsRepository: UserWithProductsRepository
) : ViewModel() {
    /*    var productListCartUiState by mutableStateOf(ProductListCartUiState())
            private set*/

    val productListCartUiState: StateFlow<ProductListCartUiState> =
        productRepository.getAllByUserProductFlow(1).map {
            ProductListCartUiState(it)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = AppContainer.TIMEOUT),
            initialValue = ProductListCartUiState()
        )
    val productListUiState: Flow<PagingData<Product>> = productRepository.loadAllProductPaged()

    /*    init {
            update()
        }*/

    /*    fun update() {
            viewModelScope.launch {
                productListCartUiState =
                    ProductListCartUiState(productRepository.getAllByUserProduct(1))
            }
        }*/

    suspend fun deleteProduct(product: Product) {
        productRepository.delete(product)
        /*update()*/
    }

    suspend fun addToCartProduct(productid: Int) {
        val user = UserWithProducts(1, productid, 1)
        userWithProductsRepository.insert(user)
        /* update()*/
    }
}

data class ProductListCartUiState(val productListCart: List<Product> = listOf())