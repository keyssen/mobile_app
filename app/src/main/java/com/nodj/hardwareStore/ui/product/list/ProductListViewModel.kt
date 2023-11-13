package com.nodj.hardwareStore.ui.product.list


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nodj.hardwareStore.db.database.AppDataContainer
import com.nodj.hardwareStore.db.models.Product
import com.nodj.hardwareStore.db.models.helperModels.AdvancedProduct
import com.nodj.hardwareStore.db.models.manyToMany.UserWithProducts
import com.nodj.hardwareStore.db.repository.repositoryInterface.ProductRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.UserWithProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductListViewModel(
    private val productRepository: ProductRepository,
    private val userWithProductsRepository: UserWithProductsRepository
) : ViewModel() {
    val productListUiState: StateFlow<ProductListUiState> = productRepository.getAll().map {
        ProductListUiState(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = AppDataContainer.TIMEOUT),
        initialValue = ProductListUiState()
    )

    val productListCartUiState: StateFlow<ProductListCartUiState> = productRepository.getAllByUserProduct(1).map {
        ProductListCartUiState(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = AppDataContainer.TIMEOUT),
        initialValue = ProductListCartUiState()
    )

    suspend fun deleteProduct(product: Product) {
        productRepository.delete(product)
    }

    suspend fun addToCartProduct(productid: Int) {
        userWithProductsRepository.insert(UserWithProducts(1, productid, 1))
    }

    fun call(): Flow<PagingData<Product>> = Pager(
        PagingConfig(
            pageSize = 10,
            prefetchDistance = 10,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            productRepository.loadAllProductPaged()
        }
    ).flow

    val productPagedData: Flow<PagingData<Product>> = call()
        .cachedIn(viewModelScope)
}

data class ProductListUiState(val productList: List<Product> = listOf())

data class ProductListCartUiState(val productListCart: List<Product> = listOf())