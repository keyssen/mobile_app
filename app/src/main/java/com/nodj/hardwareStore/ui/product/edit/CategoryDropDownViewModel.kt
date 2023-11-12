package com.nodj.hardwareStore.ui.product.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nodj.hardwareStore.db.models.Category
import com.nodj.hardwareStore.db.repository.repositoryInterface.CategoryRepository
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class CategoryDropDownViewModel(
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    var categorysListUiState by mutableStateOf(CategorysListUiState())
        private set

    var categoryUiState by mutableStateOf(CategoryUiState())
        private set

    init {
        viewModelScope.launch {
            categoryRepository.getAll().collect(){
                categorysListUiState = CategorysListUiState(it)
            }
        }
    }

    fun setCurrentCategory(categoryId: Int) {
        val category: Category? =
            categorysListUiState.categoryList.firstOrNull { category -> category.id == categoryId }
        category?.let { updateUiState(it) }
    }

    fun updateUiState(category: Category) {
        categoryUiState = CategoryUiState(
            category = category
        )
    }
}

data class CategorysListUiState(val categoryList: List<Category> = listOf())

data class CategoryUiState(
    val category: Category = Category.getEmpty()
)

fun Category.toUiState() = CategoryUiState(category = Category(id = id, name = name))