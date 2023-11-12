package com.nodj.hardwareStore.ui.product.edit

import androidx.annotation.DrawableRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.ColumnInfo
import com.nodj.hardwareStore.db.models.Product
import com.nodj.hardwareStore.db.models.manyToMany.UserWithProducts
import com.nodj.hardwareStore.db.repository.repositoryInterface.ProductRepository
import com.nodj.hardwareStore.db.repository.repositoryInterface.UserWithProductsRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProductEditViewModel(
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
                    .filterNotNull()
                    .first()
                    .toUiState(true)
            }
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
                productRepository.update(productUiState.productDetails.toProduct(productid))
            } else {
                productRepository.insert(productUiState.productDetails.toProduct())
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
    val isEntryValid: Boolean = false
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
    image = Product.toBitmap(image),
    categoryId = categoryId,
)

fun Product.toDetails(): ProductDetails = ProductDetails(
    name = name,
    price = price,
    image = Product.fromBitmap(image),
    categoryId = categoryId
)

fun Product.toUiState(isEntryValid: Boolean = false): ProductUiState = ProductUiState(
    productDetails = this.toDetails(),
    isEntryValid = isEntryValid
)