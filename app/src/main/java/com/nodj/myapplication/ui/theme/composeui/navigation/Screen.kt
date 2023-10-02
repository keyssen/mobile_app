package com.nodj.myapplication.ui.theme.composeui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.nodj.myapplication.R

enum class Screen(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector = Icons.Filled.Favorite,
    val showInBottomBar: Boolean = true
) {
    ProductList(
        "product-list", R.string.product_main_title, Icons.Filled.Home
    ),
    Search(
        "search", R.string.product_main_title, Icons.Filled.Search
    ),
    ProductView(
        "product-view/{id}", R.string.product_view_title, showInBottomBar = false
    ),
    Catalog(
        "catalog", R.string.product_catalog, Icons.Filled.List
    ),
    Cart(
        "cart", R.string.product_cart, Icons.Filled.ShoppingCart
    ),
    Favorites(
        "favorites", R.string.product_favorites, Icons.Filled.Star
    ),
    Profile(
    "profile", R.string.product_profile, Icons.Filled.AccountBox
    ),
    SignIn(
        "sign-in", R.string.product_sign_in
    ),
    SignUp(
        "sign-up", R.string.product_sign_up
    );


    companion object {
        val bottomBarItems = listOf(
            ProductList,
            Catalog,
            Cart,
            Favorites,
            Profile
        )

        fun getItem(route: String): Screen? {
            val findRoute = route.split("/").first()
            return values().find { value -> value.route.startsWith(findRoute) }
        }
    }
}