package com.nodj.hardwareStore.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

fun changeLocation(
    navController: NavController,
    route: String
) {
    navController.navigate(route) {
        popUpTo(route) {
            inclusive = true
        }
    }
}

fun changeLocationDeprecated(
    navController: NavController,
    route: String
) {
    navController.navigate(route) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}