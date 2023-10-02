package com.nodj.myapplication.ui.theme.category.composeui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nodj.myapplication.ui.theme.MyApplicationTheme
import com.nodj.myapplication.ui.theme.category.composeui.model.Category
import com.nodj.myapplication.ui.theme.category.composeui.model.getСategories
import com.nodj.myapplication.ui.theme.product.model.Product
import com.nodj.myapplication.ui.theme.product.model.getProducts

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryList(navController: NavController?) {
    val categoryList: List<Category> = getСategories()
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
            items(categoryList.size) { index ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 10.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(Color.Gray)
                        .height(50.dp)
                        .padding(all = 10.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Text("${categoryList[index].name}")
                }
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
            CategoryList(navController = null)
        }
    }
}