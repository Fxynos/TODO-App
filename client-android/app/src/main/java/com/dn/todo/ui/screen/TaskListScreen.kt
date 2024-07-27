package com.dn.todo.ui.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.dn.todo.ui.viewmodel.TaskListViewModel

@Composable
fun TaskListScreen(
    viewModel: TaskListViewModel = hiltViewModel(),
    onCreateTask: (taskId: Long) -> Unit,
    onEditTask: (taskId: Long) -> Unit
) {
    TODO()
}