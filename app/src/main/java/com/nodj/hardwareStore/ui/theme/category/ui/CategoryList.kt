package com.nodj.hardwareStore.ui.theme.category.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nodj.hardwareStore.ui.theme.MyApplicationTheme
import com.nodj.hardwareStore.ui.theme.database.AppDatabase
import com.nodj.hardwareStore.ui.theme.models.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryList(navController: NavController?) {
    val context = LocalContext.current
    val categories = remember { mutableStateListOf<Category>() }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            categories.clear()
            categories.addAll(AppDatabase.getInstance(context).categoryDao().getAll())
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
            items(categories.size) { index ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 10.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(Color.Gray)
                        .height(50.dp)
                        .padding(all = 10.dp)
                        .clickable {
//                            navController
                        },
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Text("${categories[index].name}")
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