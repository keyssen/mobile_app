package com.nodj.hardwareStore.ui.theme.navigation

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nodj.hardwareStore.ui.theme.MyApplicationTheme
import com.nodj.hardwareStore.ui.theme.UI.Search
import com.nodj.hardwareStore.ui.theme.category.ui.CategoryList
import com.nodj.hardwareStore.ui.theme.product.ui.OrderView
import com.nodj.hardwareStore.ui.theme.product.ui.ProductList
import com.nodj.hardwareStore.ui.theme.product.ui.ProductView
import com.nodj.hardwareStore.ui.theme.screen.Cart
import com.nodj.hardwareStore.ui.theme.screen.Orders
import com.nodj.hardwareStore.ui.theme.screen.Profile
import com.nodj.hardwareStore.ui.theme.screen.SignIn
import com.nodj.hardwareStore.ui.theme.screen.SignUp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(currentScreen: Screen?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(10.dp)
    ) {
        if (currentScreen == null) {
            Search()
        }
        if (currentScreen?.route == "sign-in" || currentScreen?.route == "sign-up"|| currentScreen?.route == "profile") {
            Text(stringResource(currentScreen.resourceId))
        } else {
            Search()
        }
    }
}

@Composable
fun Navbar(
    navController: NavHostController,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
) {
    val appBarBackgroundColor = MaterialTheme.colorScheme.secondary
    NavigationBar(modifier,
        containerColor = appBarBackgroundColor) {
        Screen.bottomBarItems.forEach { screen ->
            NavigationBarItem(
                icon = {
                    if (screen.icon == null){
                        if (screen.iconId != null){
                            Icon(painter = painterResource(id = screen.iconId) , contentDescription = null)
                        }
                    }
                    else{
                        Icon(screen.icon, contentDescription = null)
                    }
                       },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun Navhost(
    navController: NavHostController,
    innerPadding: PaddingValues, modifier:
    Modifier = Modifier
) {
    NavHost(
        navController,
        startDestination = Screen.ProductList.route,
        modifier.padding(innerPadding)
    ) {
        composable(Screen.ProductList.route) { ProductList(navController) }
        composable(Screen.Catalog.route) { CategoryList(navController) }
        composable(Screen.Cart.route) { Cart(navController) }
        composable(Screen.Orders.route) { Orders(navController) }
        composable(Screen.SignIn.route) { SignIn(navController) }
        composable(Screen.SignUp.route) { SignUp(navController) }
        composable(Screen.Profile.route) { Profile(navController) }
        composable(
            Screen.ProductView.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            backStackEntry.arguments?.let { ProductView(it.getInt("id")) }
        }
        composable(
            Screen.OrderView.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            backStackEntry.arguments?.let { OrderView(navController, it.getInt("id")) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavbar() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentScreen = currentDestination?.route?.let { Screen.getItem(it) }

    Scaffold(
        topBar = {
            TopBar(currentScreen)
        },
        bottomBar = {
            if (currentScreen == null || currentScreen.showInBottomBar) {
                Navbar(navController, currentDestination)
            }
        }
    ) { innerPadding ->
        Navhost(navController, innerPadding)
    }
}

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MainNavbarPreview() {
    MyApplicationTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            MainNavbar()
        }
    }
}