package com.dn.todo.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.dn.todo.ui.component.TaskCard
import com.dn.todo.ui.viewmodel.TaskListViewModel

@Composable
fun TaskListScreen(
    viewModel: TaskListViewModel = hiltViewModel(),
    onCreateTask: (taskId: Long) -> Unit,
    onEditTask: (taskId: Long) -> Unit
) {
    val tasks = viewModel.tasks.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        viewModel.refreshEvent.collect { // data-driven refresh
            tasks.refresh()
        }
    }

    LazyColumn {
        items(
            count = tasks.itemCount,
            key = { tasks[it]!!.id }
        ) {
            val item = tasks[it]!!

            Box(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                TaskCard(
                    item.title, item.description, item.isCompleted,
                    onCompletionChange = { isCompleted -> viewModel.markTask(item.id, isCompleted) }
                )
            }
        }
    }
}