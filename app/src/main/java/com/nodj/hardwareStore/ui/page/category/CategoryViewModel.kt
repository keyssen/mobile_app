package com.nodj.hardwareStore.ui.page.category

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import com.nodj.hardwareStore.R
import com.nodj.hardwareStore.common.MyViewModel
import com.nodj.hardwareStore.db.repository.repositoryInterface.CategoryRepository
import com.nodj.hardwareStore.ui.page.category.edit.CategoryUiState
import com.nodj.hardwareStore.ui.page.category.edit.toUiState

class CategoryViewModel(
    savedStateHandle: SavedStateHandle,
    private val categoryRepository: CategoryRepository,

    ) : MyViewModel() {

    var categoryUiState by mutableStateOf(CategoryUiState())
        private set

    var error by mutableStateOf(0)
        private set

    private val categoryid: Int = checkNotNull(savedStateHandle["id"])

    init {
        if (categoryid > 0) {
            runInScope(
                actionSuccess = {
                    categoryUiState = categoryRepository.getByid(categoryid)
                        .toUiState(true)
                },
                actionError = {
                    categoryUiState = CategoryUiState()
                    error = R.string.error_404
                }
            )
        }
    }
}