package com.dn.todo.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.dn.todo.ui.R
import com.dn.todo.ui.component.TaskCard
import com.dn.todo.ui.theme.AppTypography
import com.dn.todo.ui.viewmodel.TaskListViewModel
import kotlinx.coroutines.launch

private const val TAG = "TaskListScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    viewModel: TaskListViewModel = hiltViewModel(),
    snackbarState: SnackbarHostState,
    onEditTask: (taskId: Long) -> Unit
) {
    val scope = rememberCoroutineScope()
    val tasks = viewModel.tasks.collectAsLazyPagingItems()
    val lifecycleOwner = LocalLifecycleOwner.current
    val pullToRefresh = rememberPullToRefreshState() // ui indicator state

    /* Resources */
    val noConnectionMessage = stringResource(R.string.no_connection)

    /* UI Logic */

    fun showSnackbarNoConnection() =
        scope.launch { snackbarState.showSnackbar(noConnectionMessage) }

    // start loading when "pulled to refresh"
    LaunchedEffect(pullToRefresh.isRefreshing) {
        if (pullToRefresh.isRefreshing) {
            Log.d(TAG, "Pull to refresh")
            tasks.refresh()
        }
    }

    LaunchedEffect(tasks.loadState) {
        // hide refresh indicator when loaded
        if (tasks.loadState.refresh !is LoadState.Loading) {
            Log.d(TAG, "Hide pull-to-refresh indicator")
            pullToRefresh.endRefresh()
        }
        // notify if no connection
        if (tasks.loadState.hasError)
            showSnackbarNoConnection()
    }

    // subscribe to data-driven events
    LaunchedEffect(Unit) {
        viewModel.event.collect {
            when (it) {
                is TaskListViewModel.DataDrivenEvent.RefreshTaskList -> tasks.refresh()
                is TaskListViewModel.DataDrivenEvent.NavigateToTaskEdit -> onEditTask(it.taskId)
                is TaskListViewModel.DataDrivenEvent.NotifyNoConnection -> showSnackbarNoConnection()
            }
        }
    }

    // refresh when screen restarted
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME)
                tasks.refresh()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    /* Layout */

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(pullToRefresh.nestedScrollConnection)
    ) {
        if (tasks.itemCount == 0) // pull-to-refresh indicator requires scrollable placeholder
            Box(
                Modifier.fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.no_tasks),
                    style = AppTypography.titleLarge
                )
            }
        else
            LazyColumn {
                items(
                    count = tasks.itemCount,
                    key = { tasks[it]!!.id }
                ) {
                    val item = tasks[it]!!

                    Box(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                        SwipeToRemoveCard(onRemoved = { viewModel.deleteTask(item.id) }) {
                            TaskCard(
                                item.title, item.description, item.isCompleted,
                                onCheckedChange = { isCompleted ->
                                    viewModel.markTask(item.id, isCompleted)
                                },
                                onClick = { viewModel.editTask(item.id) }
                            )
                        }
                    }
                }
            }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            onClick = { viewModel.createTask() }
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_add), contentDescription = null)
        }
        PullToRefreshContainer(
            modifier = Modifier.align(Alignment.TopCenter),
            state = pullToRefresh,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToRemoveCard(
    onRemoved: () -> Unit = {},
    content: @Composable RowScope.() -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.Settled)
                return@rememberSwipeToDismissBoxState false

            onRemoved()
            true
        },
        positionalThreshold = { it * .25f }
    )
    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromEndToStart = false,
        backgroundContent = {
            val backgroundColor =
                if (dismissState.dismissDirection == SwipeToDismissBoxValue.Settled)
                    Color.Transparent
                else
                    MaterialTheme.colorScheme.errorContainer

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(shape = RoundedCornerShape(8.dp), color = backgroundColor)
                    .padding(start = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                    contentDescription = null
                )
            }
        },
        content = content
    )
}