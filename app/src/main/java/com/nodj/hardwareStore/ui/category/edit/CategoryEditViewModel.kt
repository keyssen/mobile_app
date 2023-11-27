package com.nodj.hardwareStore.ui.category.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nodj.hardwareStore.db.models.Category
import com.nodj.hardwareStore.db.repository.repositoryInterface.CategoryRepository
import kotlinx.coroutines.launch

class CategoryEditViewModel(
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

    fun updateUiState(categoryDetails: CategoryDetails) {
        categoryUiState = CategoryUiState(
            categoryDetails = categoryDetails,
            isEntryValid = validateInput(categoryDetails)
        )
    }

    suspend fun saveCategory() {
        if (validateInput()) {
            if (categoryid > 0) {
                categoryRepository.update(categoryUiState.categoryDetails.toCategory(categoryid))
            } else {
                categoryRepository.insert(categoryUiState.categoryDetails.toCategory())
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