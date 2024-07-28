package com.dn.todo.ui.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dn.todo.ui.viewmodel.TaskEditViewModel

@Composable
fun TodoApp(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.TaskList.route
    ) {
        composable(Screen.TaskList.route, Screen.TaskList.navArgs) {
            TaskListScreen(onEditTask = { taskId ->
                navController.navigate(Screen.TaskEdit.createRoute(taskId))
            })
        }
        composable(Screen.TaskEdit.route, Screen.TaskEdit.navArgs) { backStack ->
            TaskEditScreen(
                viewModel =
                    hiltViewModel<TaskEditViewModel, TaskEditViewModel.Factory> {
                        factory -> factory.create(Screen.TaskEdit.getTaskId(backStack))
                    },
                onBack = navController::navigateUp
            )
        }
    }
}