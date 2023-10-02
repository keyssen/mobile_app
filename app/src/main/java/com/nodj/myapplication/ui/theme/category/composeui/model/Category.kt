package com.nodj.myapplication.ui.theme.category.composeui.model

import java.io.Serializable

data class Category(val name: String) : Serializable

fun getСategories(): List<Category> {
    return listOf(
        Category("Category1"),
        Category("Category2"),
        Category("Category3")
    )
}
