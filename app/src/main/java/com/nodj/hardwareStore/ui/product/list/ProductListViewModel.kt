package com.nodj.hardwareStore.ui.product.list


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nodj.hardwareStore.LiveStore
import com.nodj.hardwareStore.db.models.Product
import com.nodj.hardwareStore.db.models.manyToMany.UserWithProducts
import com.nodj.hardwareStore.db.repository.repositoryInterface.IncompleteProductRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.UserWithProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

class ProductListViewModel(
    private val productRepository: IncompleteProductRepository,
    private val userWithProductsRepository: UserWithProductsRepository,
) : ViewModel() {

    var productListCartUiState by mutableStateOf(ProductListCartUiState())
        private set
    var productListUiState by mutableStateOf(ProductListUiState())
        private set

    fun update() {
        viewModelScope.launch {
            productListCartUiState =
                ProductListCartUiState(
                    productRepository.getAllByUserProduct(LiveStore.getUserId())
                )
        }
    }

    fun refresh() {
        val name = "%${LiveStore.searchRequest.value}%"
        val pagingSource = productRepository.getAll(name)
        productListUiState = ProductListUiState(pagingSource.cachedIn(viewModelScope))
    }

    suspend fun deleteProduct(product: Product) {
        productRepository.delete(product)
        update()
    }

    suspend fun addToCartProduct(productid: Int) {
        if (LiveStore.getUserId() != 0) {
            val user = UserWithProducts(LiveStore.getUserId(), productid, 1)
            userWithProductsRepository.insert(user)
        }
        update()
    }
}

data class ProductListCartUiState(val productListCart: List<Product> = listOf())

data class ProductListUiState(val productList: Flow<PagingData<Product>> = emptyFlow())