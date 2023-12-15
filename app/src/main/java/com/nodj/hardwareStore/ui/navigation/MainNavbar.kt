package com.nodj.hardwareStore.ui.navigation

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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.nodj.hardwareStore.LiveStore
import com.nodj.hardwareStore.db.models.UserRole
import com.nodj.hardwareStore.db.theme.order.OrderList
import com.nodj.hardwareStore.db.theme.order.OrderView
import com.nodj.hardwareStore.ui.MyApplicationTheme
import com.nodj.hardwareStore.ui.UI.Search
import com.nodj.hardwareStore.ui.category.CategoryList
import com.nodj.hardwareStore.ui.category.CategoryView
import com.nodj.hardwareStore.ui.category.edit.CategoryEdit
import com.nodj.hardwareStore.ui.page.cart.Cart
import com.nodj.hardwareStore.ui.page.profile.Profile
import com.nodj.hardwareStore.ui.page.report.Report
import com.nodj.hardwareStore.ui.page.signIn.SignIn
import com.nodj.hardwareStore.ui.page.signUp.SignUp
import com.nodj.hardwareStore.ui.product.ProductEdit
import com.nodj.hardwareStore.ui.product.ProductList
import com.nodj.hardwareStore.ui.product.ProductView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(currentScreen: Screen?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(10.dp)
    ) {
        if (currentScreen?.route == Screen.SignIn.route || currentScreen?.route == Screen.SignUp.route || currentScreen?.route == Screen.Profile.route) {
            Text(stringResource(currentScreen.resourceId))
        } else if (currentScreen?.route == Screen.ProductList.route) {
            Search(initValue = LiveStore.searchRequest.value ?: "",
                onDone = {
                    LiveStore.searchRequest.value = it
                })
        } else {
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
    NavigationBar(
        modifier,
        containerColor = appBarBackgroundColor
    ) {
        val user = LiveStore.user.observeAsState()
        Screen.bottomBarItems.forEach { screen ->
            if (screen.route == Screen.Report.route && user.value?.role == UserRole.ADMIN) {
                NavigationBarItem(
                    icon = {
                        if (screen.icon == null) {
                            if (screen.iconId != null) {
                                Icon(
                                    painter = painterResource(id = screen.iconId),
                                    contentDescription = null
                                )
                            }
                        } else {
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
            } else if (screen.route != Screen.Report.route) {
                NavigationBarItem(
                    icon = {
                        if (screen.icon == null) {
                            if (screen.iconId != null) {
                                Icon(
                                    painter = painterResource(id = screen.iconId),
                                    contentDescription = null
                                )
                            }
                        } else {
                            Icon(screen.icon, contentDescription = null)
                        }
                    },
                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                    onClick = {
                        if (screen.route == Screen.Profile.route && LiveStore.getUserId() == 0) {
                            navController.navigate(Screen.SignIn.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        } else {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
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
        startDestination = Screen.Catalog.route,
        modifier.padding(innerPadding)
    ) {
        composable(Screen.ProductList.route) { ProductList(navController) }
        composable(
            Screen.ProductEdit.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
            ProductEdit(navController)
        }
        composable(
            Screen.OrderView.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
            OrderView(navController)
        }
        composable(
            Screen.CategoryEdit.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
            CategoryEdit(navController)
        }
        composable(
            Screen.CategoryView.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
            CategoryView(navController)
        }
        composable(
            Screen.ProductView.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
            ProductView(navController)
        }
        composable(Screen.Catalog.route) { CategoryList(navController) }
        composable(Screen.Cart.route) { Cart(navController) }
        composable(Screen.Orders.route) { OrderList(navController) }
        composable(Screen.SignIn.route) { SignIn(navController) }
        composable(Screen.SignUp.route) { SignUp(navController) }
        composable(Screen.Profile.route) { Profile(navController) }
        composable(Screen.Report.route) { Report(navController) }
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