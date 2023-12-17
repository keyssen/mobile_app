package com.nodj.hardwareStore.ui.page.product

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.nodj.hardwareStore.R
import com.nodj.hardwareStore.common.MyViewModel
import com.nodj.hardwareStore.db.repository.repositoryInterface.ProductRepository
import com.nodj.hardwareStore.ui.page.product.edit.ProductUiState
import com.nodj.hardwareStore.ui.page.product.edit.toUiState
import kotlinx.coroutines.launch

class ProductViewModel(
    savedStateHandle: SavedStateHandle,
    private val productRepository: ProductRepository,

    ) : MyViewModel() {

    var productUiState by mutableStateOf(ProductUiState())
        private set

    var error by mutableStateOf(0)
        private set

    private val productid: Int = checkNotNull(savedStateHandle["id"])


    init {
        viewModelScope.launch {
            runInScope(
                actionSuccess = {
                    productUiState = productRepository.getProduct(productid)
                        .toUiState(true)
                },
                actionError = {
                    productUiState = ProductUiState()
                    error = R.string.error_404
                }
            )
        }
    }
}