package com.nodj.hardwareStore.ui.page.category.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import com.nodj.hardwareStore.R
import com.nodj.hardwareStore.common.MyViewModel
import com.nodj.hardwareStore.db.models.Category
import com.nodj.hardwareStore.db.repository.repositoryInterface.CategoryRepository

class CategoryEditViewModel(
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

    fun updateUiState(categoryDetails: CategoryDetails) {
        categoryUiState = CategoryUiState(
            categoryDetails = categoryDetails,
            isEntryValid = validateInput(categoryDetails)
        )
    }

    suspend fun saveCategory() {
        if (validateInput()) {
            if (categoryid > 0) {
                runInScope(
                    actionSuccess = {
                        categoryRepository.update(
                            categoryUiState.categoryDetails.toCategory(
                                categoryid
                            )
                        )
                    },
                    actionError = {
                        error = R.string.error_404
                    }
                )
            } else {
                runInScope(
                    actionSuccess = {
                        categoryRepository.insert(categoryUiState.categoryDetails.toCategory())
                    },
                    actionError = {
                        error = R.string.error_404
                    }
                )
            }
        }
    }

    private fun validateInput(uiState: CategoryDetails = categoryUiState.categoryDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }
}

data class CategoryUiState(
    val categoryDetails: CategoryDetails = CategoryDetails(),
    val isEntryValid: Boolean = false
)

data class CategoryDetails(
    val name: String = "",
)

fun CategoryDetails.toCategory(id: Int = 0): Category = Category(
    id = id,
    name = name
)

fun Category.toDetails(): CategoryDetails = CategoryDetails(
    name = name
)

fun Category.toUiState(isEntryValid: Boolean = false): CategoryUiState = CategoryUiState(
    categoryDetails = this.toDetails(),
    isEntryValid = isEntryValid
)