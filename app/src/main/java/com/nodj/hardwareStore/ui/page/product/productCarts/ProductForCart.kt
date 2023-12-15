package com.nodj.hardwareStore.ui.page.product.productCarts

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nodj.hardwareStore.R
import com.nodj.hardwareStore.db.database.AppDatabase
import com.nodj.hardwareStore.db.models.Product
import com.nodj.hardwareStore.db.models.helperModels.AdvancedProduct
import com.nodj.hardwareStore.ui.MyApplicationTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductForCart(
    advancedProduct: AdvancedProduct,
    onClickPlus: (id: Int) -> Unit,
    onClickMinus: (id: Int) -> Unit,
    onClickDelete: (id: Int) -> Unit,
    onClickViewProduct: (id: Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(top = 10.dp)
            .border(2.dp, Color.Gray, RoundedCornerShape(10.dp))
            .clickable {
                onClickViewProduct(advancedProduct.product.id)
            },
    ) {
        Image(
            modifier = Modifier
                .width(110.dp)
                .height(160.dp)
                .padding(start = 10.dp),
            bitmap = Product.toBitmap(advancedProduct.product.image).asImageBitmap(),
            contentDescription = "Продукт"
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp)
                    .padding(end = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { onClickPlus(advancedProduct.product.id) },
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .size(20.dp)
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = null
                    )
                }
                Text(
                    text = advancedProduct.count.toString(),
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .size(20.dp)
                )
                IconButton(
                    onClick = { onClickMinus(advancedProduct.product.id) },
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .size(20.dp)
                ) {
                    Icon(
                        ImageVector.vectorResource(id = R.drawable.minus),
                        contentDescription = null
                    )
                }
                IconButton(
                    onClick = { onClickDelete(advancedProduct.product.id) },
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .size(20.dp)
                ) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = null
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "${advancedProduct.product.name}"
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "${advancedProduct.product.price}"
                )
            }

        }
    }
}