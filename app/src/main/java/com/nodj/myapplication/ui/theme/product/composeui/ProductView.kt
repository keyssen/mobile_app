package com.nodj.myapplication.ui.theme.product.composeui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nodj.myapplication.R
import com.nodj.myapplication.ui.theme.MyApplicationTheme
import com.nodj.myapplication.ui.theme.product.model.getProducts

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductView(id: Int) {
    val product = getProducts()[id]
    Column(
        Modifier
            .fillMaxWidth()
            .padding(all = 10.dp)
    ) {
        OutlinedTextField(modifier = Modifier.fillMaxWidth(),
            value = product.name, onValueChange = {}, readOnly = true,
            label = {
                Text(stringResource(id = R.string.product_name))
            }
        )
        OutlinedTextField(modifier = Modifier.fillMaxWidth(),
            value = product.price.toString(), onValueChange = {}, readOnly = true,
            label = {
                Text(stringResource(id = R.string.product_price))
            }
        )
        OutlinedTextField(modifier = Modifier.fillMaxWidth(),
            value = product.category, onValueChange = {}, readOnly = true,
            label = {
                Text(stringResource(id = R.string.product_price))
            }
        )
        product.characteristics.forEachIndexed() { index, characteristic ->
            OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                value = characteristic.value, onValueChange = {}, readOnly = true,
                label = {
                    Text(characteristic.name)
                }
            )
        }
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