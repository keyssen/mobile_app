package com.nodj.hardwareStore.ui.page.product.list


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nodj.hardwareStore.LiveStore
import com.nodj.hardwareStore.R
import com.nodj.hardwareStore.common.MyViewModel
import com.nodj.hardwareStore.db.models.Product
import com.nodj.hardwareStore.db.models.helperModels.AdvancedProduct
import com.nodj.hardwareStore.db.models.manyToMany.UserWithProducts
import com.nodj.hardwareStore.db.repository.repositoryInterface.ProductRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.UserWithProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

class ProductListViewModel(
    private val productRepository: ProductRepository,
    private val userWithProductsRepository: UserWithProductsRepository,
) : MyViewModel() {
    var productListUiState by mutableStateOf(ProductListUiState())
        private set

    var error by mutableStateOf(0)
        private set

    fun refresh() {
        val name = "%${LiveStore.searchRequest.value}%"
        viewModelScope.launch {
            productListUiState =
                ProductListUiState(productRepository.getAll(name).cachedIn(viewModelScope))
            if (productListUiState.productList.count() == 0) {
                error = R.string.error_404
            } else {
                error = 0
            }
        }
    }

    fun clearError() {
        error = 0
    }

    suspend fun deleteProduct(product: Product) {
        runInScope(
            actionSuccess = {
                productRepository.delete(product)
            }, actionError = {
                error = R.string.error_404
            }
        )
    }

    suspend fun addToCartProduct(productid: Int) {
        if (LiveStore.getUserId() != 0) {
            val user = UserWithProducts(LiveStore.getUserId(), productid, 1)
            runInScope(
                actionSuccess = {
                    userWithProductsRepository.insert(user)
                }, actionError = {
                    error = R.string.error_404
                }
            )
        }
    }
}

data class ProductListCartUiState(val productListCart: List<Product> = listOf())

data class ProductListUiState(val productList: Flow<PagingData<AdvancedProduct>> = emptyFlow())