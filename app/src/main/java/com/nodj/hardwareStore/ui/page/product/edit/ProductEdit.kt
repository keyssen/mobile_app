package com.nodj.hardwareStore.ui.product

import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
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
import com.nodj.hardwareStore.ui.page.product.edit.CategoryDropDownViewModel
import com.nodj.hardwareStore.ui.page.product.edit.CategoryUiState
import com.nodj.hardwareStore.ui.page.product.edit.CategorysListUiState
import com.nodj.hardwareStore.ui.page.product.edit.ProductDetails
import com.nodj.hardwareStore.ui.page.product.edit.ProductEditViewModel
import com.nodj.hardwareStore.ui.page.product.edit.ProductUiState
import kotlinx.coroutines.launch
import okio.IOException
import java.io.ByteArrayOutputStream
import java.io.FileInputStream

@Composable
fun ProductEdit(
    navController: NavController,
    viewModel: ProductEditViewModel = viewModel(factory = AppViewModelProvider.Factory),
    categoryViewModel: CategoryDropDownViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    categoryViewModel.setCurrentCategory(viewModel.productUiState.productDetails.categoryId)
    if (categoryViewModel.error != 0) {
        ErrorPlaceholder(
            message = stringResource(categoryViewModel.error),
            onBack = { navController.popBackStack() }
        )
        return
    }
    when (viewModel.apiStatus) {
        ApiStatus.DONE -> {
            ProductEdit(
                productUiState = viewModel.productUiState,
                categoryUiState = categoryViewModel.categoryUiState,
                categorysListUiState = categoryViewModel.categoriesListUiState,
                onClick = {
                    coroutineScope.launch {
                        viewModel.saveProduct()
                        navController.popBackStack()
                    }
                },
                onUpdate = viewModel::updateUiState,
                onCategoryUpdate = categoryViewModel::updateUiState
            )
        }

        ApiStatus.LOADING -> LoadingPlaceholder()
        else -> ErrorPlaceholder(
            message = viewModel.apiError,
            onBack = { navController.popBackStack() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryDropDown(
    categoryUiState: CategoryUiState,
    categorysListUiState: CategorysListUiState,
    onCategoryUpdate: (Category) -> Unit
) {
    var expanded: Boolean by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        modifier = Modifier
            .padding(top = 7.dp),
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        TextField(
            value = categoryUiState.category?.name
                ?: stringResource(id = R.string.product_category_not_select),
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color.White)
                .exposedDropdownSize()
        ) {
            categorysListUiState.categoryList.forEach { category ->
                DropdownMenuItem(
                    text = {
                        Text(text = category.name)
                    },
                    onClick = {
                        onCategoryUpdate(category)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ImageSelectionScreen(
    productUiState: ProductUiState,
    onUpdate: (ProductDetails) -> Unit,
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let { saveImageToInternalStorage(context, it) }
            onUpdate(
                productUiState.productDetails.copy(
                    image = convertFileInputStreamToByteArray(
                        context.openFileInput("image.jpg")
                    )!!
                )
            )
        }
    )
    Button(onClick = {
        launcher.launch("image/*")
    }) {
        Text(text = "Select Image")
    }
}

fun saveImageToInternalStorage(context: Context, uri: Uri) {
    val inputStream = context.contentResolver.openInputStream(uri)
    val outputStream = context.openFileOutput("image.jpg", Context.MODE_PRIVATE)
    inputStream?.use { input ->
        outputStream.use { output ->
            input.copyTo(output)
            println(output.toString())
        }
    }
}

fun convertFileInputStreamToByteArray(fileInputStream: FileInputStream): ByteArray? {
    try {
        val buffer = ByteArray(1024) // Adjust the buffer size as needed
        val byteArrayOutputStream = ByteArrayOutputStream()

        var bytesRead: Int
        while (fileInputStream.read(buffer).also { bytesRead = it } != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead)
        }

        return byteArrayOutputStream.toByteArray()
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        try {
            // Close the FileInputStream when done
            fileInputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return null
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductEdit(
    productUiState: ProductUiState,
    categoryUiState: CategoryUiState,
    categorysListUiState: CategorysListUiState,
    onClick: () -> Unit,
    onUpdate: (ProductDetails) -> Unit,
    onCategoryUpdate: (Category) -> Unit
) {
    val context = LocalContext.current
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
                ImageSelectionScreen(
                    productUiState,
                    onUpdate
                )
            }
            item {
                OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                    value = productUiState.productDetails.name,
                    onValueChange = { onUpdate(productUiState.productDetails.copy(name = it)) },
                    label = {
                        Text(stringResource(id = R.string.name))
                    }
                )
            }
            item {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = productUiState.productDetails.price.toString(),
                    onValueChange = { onUpdate(productUiState.productDetails.copy(price = it.toDouble())) },
                    label = {
                        Text(stringResource(id = R.string.product_price))
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            item {
                CategoryDropDown(
                    categoryUiState = categoryUiState,
                    categorysListUiState = categorysListUiState,
                    onCategoryUpdate = {
                        onUpdate(productUiState.productDetails.copy(categoryId = it.id!!))
                        onCategoryUpdate(it)
                    }
                )
            }
            item {
                Button(
                    onClick = onClick,
                    enabled = productUiState.isEntryValid,
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
fun ProductEditPreview() {
    MyApplicationTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
        }
    }
}