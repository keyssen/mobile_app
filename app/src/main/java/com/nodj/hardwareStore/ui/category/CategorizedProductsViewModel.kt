package com.nodj.hardwareStore.ui.category


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nodj.hardwareStore.db.database.AppDataContainer
import com.nodj.hardwareStore.db.models.Category
import com.nodj.hardwareStore.db.models.Order
import com.nodj.hardwareStore.db.models.Product
import com.nodj.hardwareStore.db.models.helperModels.AdvancedProduct
import com.nodj.hardwareStore.db.models.manyToMany.UserWithProducts
import com.nodj.hardwareStore.db.repository.repositoryInterface.CategoryRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.OrderRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.ProductRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.UserWithProductsRepository
import com.nodj.hardwareStore.db.service.OrderService
import com.nodj.hardwareStore.ui.page.orders.order.OrderUiState
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

class CategorizedProductsViewModel(
    private val productRepository: ProductRepository,
    private val userWithProductsRepository: UserWithProductsRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var categorizedProductsUiState by mutableStateOf(CategorizedProductsUiState())
        private set

    private val categoryId: Int = checkNotNull(savedStateHandle["id"])


    val productListCartUiState: StateFlow<ProductListCartUiState> = productRepository.getAllByUserProduct(1).map {
        ProductListCartUiState(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = AppDataContainer.TIMEOUT),
        initialValue = ProductListCartUiState()
    )

    init {
        viewModelScope.launch {
            if (categoryId > 0) {
                categorizedProductsUiState = CategorizedProductsUiState(productRepository.getByCategory(categoryId))
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