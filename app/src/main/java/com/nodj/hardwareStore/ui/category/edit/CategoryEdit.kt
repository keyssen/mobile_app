package com.nodj.hardwareStore.ui.category.edit

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nodj.hardwareStore.R
import com.nodj.hardwareStore.ui.AppViewModelProvider
import com.nodj.hardwareStore.ui.MyApplicationTheme
import kotlinx.coroutines.launch

@Composable
fun CategoryEdit(
    navController: NavController,
    viewModel: CategoryEditViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val coroutineScope = rememberCoroutineScope()
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryEdit(
    categoryUiState: CategoryUiState,
    onClick: () -> Unit,
    onUpdate: (CategoryDetails) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),

        // content padding
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
//            CategoryView(id = 0)
        }
    }
}