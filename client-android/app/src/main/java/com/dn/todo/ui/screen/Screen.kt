package com.dn.todo.ui.screen

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val navArgs: List<NamedNavArgument> = emptyList()
) {
    data object TaskList: Screen("list")

    data object TaskEdit: Screen(
        route = "edit/{taskId}",
        navArgs = listOf(
            navArgument("taskId") {
                type = NavType.LongType
            }
        )
    ) {
        fun createRoute(taskId: Long) = "edit/$taskId"
        fun getTaskId(backStack: NavBackStackEntry): Long =
            backStack.arguments!!.getLong("taskId")
    }
}