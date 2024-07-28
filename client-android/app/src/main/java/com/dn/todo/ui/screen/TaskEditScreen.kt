package com.dn.todo.ui.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.dn.todo.ui.viewmodel.TaskEditViewModel

@Composable
fun TaskEditScreen(
    viewModel: TaskEditViewModel = hiltViewModel(),
    onTaskSaved: (taskId: Long) -> Unit,
    onTaskDeleted: (taskId: Long) -> Unit
) {
    Text(text = "Заглушка") // TODO
}