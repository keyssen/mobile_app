package com.nodj.hardwareStore.ui.page.product.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import com.nodj.hardwareStore.R
import com.nodj.hardwareStore.common.MyViewModel
import com.nodj.hardwareStore.db.models.Product
import com.nodj.hardwareStore.db.repository.repositoryInterface.ProductRepository

class ProductEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val productRepository: ProductRepository,

    ) : MyViewModel() {

    var productUiState by mutableStateOf(ProductUiState())
        private set

    private val productid: Int = checkNotNull(savedStateHandle["id"])

    var error by mutableStateOf(0)
        private set

    init {
        if (productid > 0) {
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

    fun updateUiState(productDetails: ProductDetails) {
        productUiState = ProductUiState(
            productDetails = productDetails,
            isEntryValid = validateInput(productDetails)
        )
    }

    suspend fun saveProduct() {
        if (validateInput()) {
            if (productid > 0) {
                runInScope(
                    actionSuccess = {
                        productRepository.update(productUiState.productDetails.toProduct(productid))
                    },
                    actionError = {
                        error = R.string.error_404
                    }
                )

            } else {
                runInScope(
                    actionSuccess = {
                        productRepository.insert(productUiState.productDetails.toProduct())
                    },
                    actionError = {
                        error = R.string.error_404
                    }
                )
            }
        }
    }

    private fun validateInput(uiState: ProductDetails = productUiState.productDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()
                    && price > 0
                    && !image.isEmpty()
                    && categoryId > 0
        }
    }
}

data class ProductUiState(
    val productDetails: ProductDetails = ProductDetails(),
    val isEntryValid: Boolean = false,
)

data class ProductDetails(
    val name: String = "",
    val price: Double = 0.0,
    val image: ByteArray = byteArrayOf(),
    val categoryId: Int = 0,
)

fun ProductDetails.toProduct(id: Int = 0): Product = Product(
    id = id,
    name = name,
    price = price,
    image = image,
    categoryId = categoryId,
)

fun Product.toDetails(): ProductDetails = ProductDetails(
    name = name,
    price = price,
    image = image,
    categoryId = categoryId!!
)

fun Product.toUiState(isEntryValid: Boolean = false): ProductUiState = ProductUiState(
    productDetails = this.toDetails(),
    isEntryValid = isEntryValid
)