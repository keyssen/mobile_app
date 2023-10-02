package com.nodj.myapplication.ui.theme.screen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nodj.myapplication.R
import com.nodj.myapplication.ui.theme.MyApplicationTheme
import com.nodj.myapplication.ui.theme.UI.Search
import com.nodj.myapplication.ui.theme.composeui.navigation.Screen
import com.nodj.myapplication.ui.theme.product.composeui.ProductList
import com.nodj.myapplication.ui.theme.product.model.Product
import com.nodj.myapplication.ui.theme.product.model.getProducts

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Favorites(navController: NavController?) {
    Column(){
        ProductList(navController, "favorite")
        Box(
            modifier = Modifier
                .padding(all = 10.dp)
                .clip(RoundedCornerShape(15.dp))
                .fillMaxWidth()
                .background(Color.Gray)
                .height(100.dp)
                .padding(all = 10.dp),
        ) {
            Column(){
                Text("Цена: 20000", textAlign = TextAlign.Start)
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Button(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .width(100.dp)
                            .height(40.dp)
                            .align(Alignment.CenterHorizontally),
                        onClick = {}) {
                        Text("Купить")
                    }
                }
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FavoritesPreview() {
    MyApplicationTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Favorites(navController = null)
        }
    }
}