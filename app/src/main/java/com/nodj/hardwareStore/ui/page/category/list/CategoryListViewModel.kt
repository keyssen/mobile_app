package com.nodj.hardwareStore.ui.page.category.list


import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.nodj.hardwareStore.db.models.Category
import com.nodj.hardwareStore.db.repository.repositoryInterface.CategoryRepository
import kotlinx.coroutines.flow.Flow

class CategoryListViewModel(
    private val categryRepository: CategoryRepository,
) : ViewModel() {

    val categoryListUiState: Flow<PagingData<Category>> = categryRepository.getAllCategories()

    suspend fun deleteCategoryt(categoryId: Int) {
        categryRepository.delete(Category(id = categoryId, name = ""))
    }
}