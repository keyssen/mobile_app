package com.nodj.hardwareStore.ui.page.category.list


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.paging.PagingData
import com.nodj.hardwareStore.R
import com.nodj.hardwareStore.common.MyViewModel
import com.nodj.hardwareStore.db.models.Category
import com.nodj.hardwareStore.db.repository.repositoryInterface.CategoryRepository
import kotlinx.coroutines.flow.Flow

class CategoryListViewModel(
    private val categryRepository: CategoryRepository,
) : MyViewModel() {

    val categoryListUiState: Flow<PagingData<Category>> = categryRepository.getAllCategories()

    var error by mutableStateOf(0)
        private set

    suspend fun deleteCategoryt(categoryId: Int) {
        runInScope(
            actionSuccess = {
                categryRepository.delete(Category(id = categoryId, name = ""))
            },
            actionError = {
                error = R.string.error_404
            }
        )

    }
}