package com.nodj.hardwareStore.ui.page.category


import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nodj.hardwareStore.R
import com.nodj.hardwareStore.api.ApiStatus
import com.nodj.hardwareStore.common.AppViewModelProvider
import com.nodj.hardwareStore.db.models.Category
import com.nodj.hardwareStore.ui.MyApplicationTheme
import com.nodj.hardwareStore.ui.UI.ErrorPlaceholder
import com.nodj.hardwareStore.ui.UI.LoadingPlaceholder
import com.nodj.hardwareStore.ui.page.category.edit.CategoryEditViewModel
import com.nodj.hardwareStore.ui.page.category.edit.CategoryUiState
import com.nodj.hardwareStore.ui.page.category.edit.toUiState
import com.nodj.hardwareStore.ui.page.product.Product

@Composable
fun Category(
    navController: NavController,
    viewModel: CategoryEditViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    when (viewModel.apiStatus) {
        ApiStatus.DONE -> {
            Category(
                categoryUiState = viewModel.categoryUiState,
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
fun Category(
    categoryUiState: CategoryUiState,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(all = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(modifier = Modifier.fillMaxWidth(),
            value = categoryUiState.categoryDetails.name, onValueChange = {}, readOnly = true,
            label = {
                Text(stringResource(id = R.string.name))
            }
        )
    }
}

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProductViewPreview() {
    MyApplicationTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Category(
                categoryUiState = Category.getEmpty().toUiState(),
            )
        }
    }
}
