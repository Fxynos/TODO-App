package com.dn.todo.ui.screen

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val navArgs: List<NamedNavArgument> = emptyList()
) {
    data object TaskList: Screen("list")

    data object TaskEdit: Screen(
        route = "edit/{taskId}?new={new}",
        navArgs = listOf(
            navArgument("taskId") {
                type = NavType.LongType
            },
            navArgument("new") {
                type = NavType.BoolType
            }
        )
    ) {
        fun createRoute(taskId: Long, isTaskNew: Boolean) = "edit/$taskId?new=$isTaskNew"
    }
}