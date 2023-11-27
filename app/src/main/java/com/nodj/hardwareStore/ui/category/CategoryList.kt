package com.nodj.hardwareStore.ui.category

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.nodj.hardwareStore.common.AppViewModelProvider
import com.nodj.hardwareStore.db.models.Category
import com.nodj.hardwareStore.ui.MyApplicationTheme
import com.nodj.hardwareStore.ui.navigation.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryList(
    navController: NavController,
    viewModel: CategoryListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val categoryListUiState = viewModel.categoryListUiState.collectAsLazyPagingItems()
    Scaffold(
        topBar = {},
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val route = Screen.CategoryEdit.route.replace("{id}", 0.toString())
                    navController.navigate(route)
                },
            ) {
                Icon(Icons.Filled.Add, "Добавить")
            }
        }
    ) { innerPadding ->
        CategoryList(
            modifier = Modifier.padding(innerPadding),
            categoryList = categoryListUiState,
            onClickDelete = { id: Int ->
                coroutineScope.launch {
                    viewModel.deleteCategoryt(id)
                }
            },
            onClickEdit = { id: Int ->
                val route = Screen.CategoryEdit.route.replace("{id}", id.toString())
                navController.navigate(route)
            },
            onClickCategoryView = { id: Int ->
                val route = Screen.CategoryView.route.replace("{id}", id.toString())
                navController.navigate(route)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryList(
    modifier: Modifier,
    categoryList: LazyPagingItems<Category>,
    onClickDelete: (id: Int) -> Unit,
    onClickEdit: (id: Int) -> Unit,
    onClickCategoryView: (id: Int) -> Unit
) {
    Column(modifier = modifier) {
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
                items(
                    count = categoryList.itemCount,
                    key = categoryList.itemKey(),
                    contentType = categoryList.itemContentType()
                ) { index ->
                    val category = categoryList[index]
                    category?.let {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = 10.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .background(Color.Gray)
                                .height(50.dp)
                                .padding(all = 10.dp)
                                .clickable {
                                    onClickCategoryView(category.id)
                                },
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${category.name}")
                            IconButton(onClick = { onClickDelete(category.id) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Удалить")
                            }
                            IconButton(onClick = { onClickEdit(category.id) }) {
                                Icon(Icons.Filled.Edit, contentDescription = "Изменить")
                            }
                        }
                    }
                }
            }
        )
    }
}

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProductListPreview() {
    MyApplicationTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
//            CategoryList(navController = null)
        }
    }
}