package com.nodj.hardwareStore.ui.category


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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Date

class CategoryListViewModel(
    private val categryRepository: CategoryRepository,
) : ViewModel() {

    val categoryListUiState: StateFlow<CategoryListUiState> = categryRepository.getAll().map {
        CategoryListUiState(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = AppDataContainer.TIMEOUT),
        initialValue = CategoryListUiState()
    )

    suspend fun deleteCategoryt(categoryId: Int) {
        categryRepository.delete(Category(id = categoryId, name=""))
    }
}

data class CategoryListUiState(val categoryList: List<Category> = listOf())