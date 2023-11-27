package com.nodj.hardwareStore.ui.product

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nodj.hardwareStore.R
import com.nodj.hardwareStore.db.models.Category
import com.nodj.hardwareStore.db.models.Product
import com.nodj.hardwareStore.common.AppViewModelProvider
import com.nodj.hardwareStore.ui.MyApplicationTheme
import com.nodj.hardwareStore.ui.product.edit.CategoryDropDownViewModel
import com.nodj.hardwareStore.ui.product.edit.CategoryUiState
import com.nodj.hardwareStore.ui.product.edit.ProductEditViewModel
import com.nodj.hardwareStore.ui.product.edit.ProductUiState
import com.nodj.hardwareStore.ui.product.edit.toUiState

@Composable
fun ProductView(
    navController: NavController,
    viewModel: ProductEditViewModel = viewModel(factory = AppViewModelProvider.Factory),
    categoryViewModel: CategoryDropDownViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    categoryViewModel.setCurrentCategory(viewModel.productUiState.productDetails.categoryId)
    ProductView(
        productUiState = viewModel.productUiState,
        categoryUiState = categoryViewModel.categoryUiState,
        onClick = {
//            coroutineScope.launch {
//                viewModel.saveProduct()
//                navController.popBackStack()
//            }
        },
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductView(
    productUiState: ProductUiState,
    categoryUiState: CategoryUiState,
    onClick: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(all = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (productUiState.productDetails.image.size > 0){
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                bitmap = Product.toBitmap(productUiState.productDetails.image).asImageBitmap(),
//            painter = painterResource(if (productUiState.productDetails.imageId > 0) productUiState.productDetails.imageId else R.drawable.i2 ),
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
            value = productUiState.productDetails.price.toString(), onValueChange = {}, readOnly = true,
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
            ProductView(
                productUiState = Product.getEmpty().toUiState(true),
                categoryUiState = Category.getEmpty().toUiState(),
                onClick = {}
            )
        }
    }
}
