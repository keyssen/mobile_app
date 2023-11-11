package com.nodj.hardwareStore.ui.product

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nodj.hardwareStore.R
import com.nodj.hardwareStore.db.database.AppDatabase
import com.nodj.hardwareStore.db.models.Product
import com.nodj.hardwareStore.ui.MyApplicationTheme
import com.nodj.hardwareStore.ui.navigation.Screen
import com.nodj.hardwareStore.ui.product.productCarts.ProductForCatalog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductList(navController: NavController?, type: String = "catalog", categoryId: Int = -1) {
    val context = LocalContext.current
    val products = remember { mutableStateListOf<Product>() }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            products.clear()
            if (categoryId == -1) {
                products.addAll(AppDatabase.getInstance(context).productDao().getAll())
            } else{
                products.addAll(AppDatabase.getInstance(context).productDao().getByCategory(categoryId))
            }
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),

        // content padding
        contentPadding = PaddingValues(
            start = 12.dp,
            top = 16.dp,
            end = 12.dp,
            bottom = 16.dp
        ),
        content = {
            items(products.size) { index ->
                ProductForCatalog(navController = navController, product = products[index])
            }
        }
    )
}

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun StudentListPreview() {
    MyApplicationTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            ProductList(navController = null)
        }
    }
}