package com.nodj.hardwareStore.ui.page.category.edit

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nodj.hardwareStore.R
import com.nodj.hardwareStore.api.ApiStatus
import com.nodj.hardwareStore.common.AppViewModelProvider
import com.nodj.hardwareStore.ui.MyApplicationTheme
import com.nodj.hardwareStore.ui.UI.ErrorPlaceholder
import com.nodj.hardwareStore.ui.UI.LoadingPlaceholder
import com.nodj.hardwareStore.ui.page.category.Category
import kotlinx.coroutines.launch

@Composable
fun CategoryEdit(
    navController: NavController,
    viewModel: CategoryEditViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val coroutineScope = rememberCoroutineScope()
    when (viewModel.apiStatus) {
        ApiStatus.DONE -> {
            CategoryEdit(
                categoryUiState = viewModel.categoryUiState,
                onClick = {
                    coroutineScope.launch {
                        viewModel.saveCategory()
                        navController.popBackStack()
                    }
                },
                onUpdate = viewModel::updateUiState,
            )
        }
        ApiStatus.LOADING -> LoadingPlaceholder()
        else -> ErrorPlaceholder(
            message = stringResource(viewModel.error),
            onBack = { navController.popBackStack() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryEdit(
    categoryUiState: CategoryUiState,
    onClick: () -> Unit,
    onUpdate: (CategoryDetails) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        contentPadding = PaddingValues(
            start = 10.dp,
            top = 10.dp,
            end = 10.dp,
            bottom = 10.dp
        ),
        content = {
            item {
                OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                    value = categoryUiState.categoryDetails.name,
                    onValueChange = { onUpdate(categoryUiState.categoryDetails.copy(name = it)) },
                    label = {
                        Text(stringResource(id = R.string.name))
                    }
                )
            }
            item {
                Button(
                    onClick = onClick,
                    enabled = categoryUiState.isEntryValid,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.save))
                }
            }
        }
    )
}

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CategoryEditPreview() {
    MyApplicationTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
        }
    }
}