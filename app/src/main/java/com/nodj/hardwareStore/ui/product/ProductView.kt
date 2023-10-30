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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nodj.hardwareStore.R
import com.nodj.hardwareStore.db.database.AppDatabase
import com.nodj.hardwareStore.db.models.Category
import com.nodj.hardwareStore.db.models.Product
import com.nodj.hardwareStore.ui.MyApplicationTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductView(id: Int) {
    val context = LocalContext.current
    var product = remember { mutableStateOf<Product>(Product.getEmpty()) }
    var category = remember { mutableStateOf<Category>(Category.getEmpty()) }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            product.value = AppDatabase.getInstance(context).productDao().getByid(id)
            category.value = AppDatabase.getInstance(context).categoryDao().getByid(product.value.categoryId)

        }
    }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(all = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            painter = painterResource(product.value.imageId),
            contentDescription = "Продукт"
        )
        OutlinedTextField(modifier = Modifier.fillMaxWidth(),
            value = product.value.name, onValueChange = {}, readOnly = true,
            label = {
                Text(stringResource(id = R.string.product_name))
            }
        )
        OutlinedTextField(modifier = Modifier.fillMaxWidth(),
            value = product.value.price.toString(), onValueChange = {}, readOnly = true,
            label = {
                Text(stringResource(id = R.string.product_price))
            }
        )
        OutlinedTextField(modifier = Modifier.fillMaxWidth(),
            value = category.value.name, onValueChange = {}, readOnly = true,
            label = {
                Text(stringResource(id = R.string.product_category))
            }
        )
    }
}

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun StudentViewPreview() {
    MyApplicationTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            ProductView(id = 0)
        }
    }
}