package com.nodj.hardwareStore.ui.page.product.edit

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.nodj.hardwareStore.R
import com.nodj.hardwareStore.common.MyViewModel
import com.nodj.hardwareStore.db.models.Category
import com.nodj.hardwareStore.db.repository.repositoryInterface.CategoryRepository

class CategoryDropDownViewModel(
    private val categoryRepository: CategoryRepository
) : MyViewModel() {
    var categoriesListUiState by mutableStateOf(CategorysListUiState())
        private set

    var categoryUiState by mutableStateOf(CategoryUiState())
        private set

    var error by mutableStateOf(0)
        private set

    init {
        runInScope(
            actionSuccess = {
                categoriesListUiState = CategorysListUiState(categoryRepository.getAll())
            },
            actionError = {
                categoriesListUiState = CategorysListUiState()
                error = R.string.error_404
            }
        )
    }

    fun setCurrentCategory(categoryId: Int) {
        val category: Category? =
            categoriesListUiState.categoryList.firstOrNull { category -> category.id == categoryId }
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