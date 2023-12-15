package com.nodj.hardwareStore.ui.page.category

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nodj.hardwareStore.db.repository.repositoryInterface.CategoryRepository
import com.nodj.hardwareStore.ui.page.category.edit.CategoryUiState
import com.nodj.hardwareStore.ui.page.category.edit.toUiState
import kotlinx.coroutines.launch

class CategoryViewModel(
    savedStateHandle: SavedStateHandle,
    private val categoryRepository: CategoryRepository,

    ) : ViewModel() {

    var categoryUiState by mutableStateOf(CategoryUiState())
        private set

    private val categoryid: Int = checkNotNull(savedStateHandle["id"])

    init {
        viewModelScope.launch {
            if (categoryid > 0) {
                categoryUiState = categoryRepository.getByid(categoryid)
                    .toUiState(true)
            }
        }
    }
}