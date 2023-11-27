package com.nodj.hardwareStore.ui.category


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nodj.hardwareStore.db.models.Product
import com.nodj.hardwareStore.db.models.manyToMany.UserWithProducts
import com.nodj.hardwareStore.db.repository.repositoryInterface.IncompleteProductRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.UserWithProductsRepository
import kotlinx.coroutines.launch

class CategorizedProductsViewModel(
    private val productRepository: IncompleteProductRepository,
    private val userWithProductsRepository: UserWithProductsRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var categorizedProductsUiState by mutableStateOf(CategorizedProductsUiState())
        private set

    private val categoryId: Int = checkNotNull(savedStateHandle["id"])


//    val productListCartUiState: StateFlow<ProductListCartUiState> =
//        productRepository.getAllByUserProduct(1).map {
//            ProductListCartUiState(it)
//        }.stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = AppContainer.TIMEOUT),
//            initialValue = ProductListCartUiState()
//        )

    init {
        viewModelScope.launch {
            if (categoryId > 0) {
                categorizedProductsUiState =
                    CategorizedProductsUiState(productRepository.getByCategory(categoryId))
            }
        }
    }

    suspend fun deleteProduct(product: Product) {
        productRepository.delete(product)
    }

    suspend fun addToCartProduct(productid: Int) {
        userWithProductsRepository.insert(UserWithProducts(1, productid, 1))
    }
}

data class CategorizedProductsUiState(val listProducts: List<Product> = listOf())