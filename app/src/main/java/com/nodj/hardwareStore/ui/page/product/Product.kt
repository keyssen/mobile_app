package com.nodj.hardwareStore.ui.page.product

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nodj.hardwareStore.R
import com.nodj.hardwareStore.common.AppViewModelProvider
import com.nodj.hardwareStore.db.models.Category
import com.nodj.hardwareStore.db.models.Product
import com.nodj.hardwareStore.ui.MyApplicationTheme
import com.nodj.hardwareStore.ui.page.product.edit.CategoryDropDownViewModel
import com.nodj.hardwareStore.ui.page.product.edit.CategoryUiState
import com.nodj.hardwareStore.ui.page.product.edit.ProductUiState
import com.nodj.hardwareStore.ui.page.product.edit.toUiState

@Composable
fun Product(
    navController: NavController,
    viewModel: ProductViewModel = viewModel(factory = AppViewModelProvider.Factory),
    categoryViewModel: CategoryDropDownViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    categoryViewModel.setCurrentCategory(viewModel.productUiState.productDetails.categoryId)
    Product(
        productUiState = viewModel.productUiState,
        categoryUiState = categoryViewModel.categoryUiState,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Product(
    productUiState: ProductUiState,
    categoryUiState: CategoryUiState,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(all = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (productUiState.productDetails.image.size > 0) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                bitmap = Product.toBitmap(productUiState.productDetails.image).asImageBitmap(),
                contentDescription = "Продукт"
            )
        }
        OutlinedTextField(modifier = Modifier.fillMaxWidth(),
            value = productUiState.productDetails.name, onValueChange = {}, readOnly = true,
            label = {
                Text(stringResource(id = R.string.name))
            }
        )
        OutlinedTextField(modifier = Modifier.fillMaxWidth(),
            value = productUiState.productDetails.price.toString(),
            onValueChange = {},
            readOnly = true,
            label = {
                Text(stringResource(id = R.string.product_price))
            }
        )
        OutlinedTextField(modifier = Modifier.fillMaxWidth(),
            value = categoryUiState.category.name, onValueChange = {}, readOnly = true,
            label = {
                Text(stringResource(id = R.string.product_category))
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
            Product(
                productUiState = Product.getEmpty().toUiState(true),
                categoryUiState = Category.getEmpty().toUiState(),
            )
        }
    }
}
