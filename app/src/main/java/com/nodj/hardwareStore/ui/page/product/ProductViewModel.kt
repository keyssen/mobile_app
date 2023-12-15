package com.nodj.hardwareStore.ui.page.product

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nodj.hardwareStore.db.repository.repositoryInterface.ProductRepository
import com.nodj.hardwareStore.ui.page.product.edit.ProductUiState
import com.nodj.hardwareStore.ui.page.product.edit.toUiState
import kotlinx.coroutines.launch

class ProductViewModel(
    savedStateHandle: SavedStateHandle,
    private val productRepository: ProductRepository,

    ) : ViewModel() {

    var productUiState by mutableStateOf(ProductUiState())
        private set

    private val productid: Int = checkNotNull(savedStateHandle["id"])

    init {
        viewModelScope.launch {
            if (productid > 0) {
                productUiState = productRepository.getProduct(productid)
                    .toUiState(true)
            }
        }
    }
}