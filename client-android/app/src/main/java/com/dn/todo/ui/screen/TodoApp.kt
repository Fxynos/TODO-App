package com.dn.todo.ui.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun TodoApp(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = ""
    ) {

    }
}