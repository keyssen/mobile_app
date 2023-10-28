package com.nodj.hardwareStore.ui.theme.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import com.nodj.hardwareStore.R

enum class Screen(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector? = null,
    val showInBottomBar: Boolean = true,
    @DrawableRes val iconId: Int? = null,
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
    OrderView(
        "order-view/{id}", R.string.product_orders, showInBottomBar = false
    ),
    Catalog(
        "catalog", R.string.product_catalog, Icons.Filled.List
    ),
    Cart(
        "cart", R.string.product_cart, Icons.Filled.ShoppingCart
    ),
    Orders(
        "orders", R.string.product_orders, iconId = R.drawable.list_check_icon
    ),
    Profile(
    "profile", R.string.product_profile, Icons.Filled.Person
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
            Orders,
            Profile
        )

        fun getItem(route: String): Screen? {
            val findRoute = route.split("/").first()
            return values().find { value -> value.route.startsWith(findRoute) }
        }
    }
}