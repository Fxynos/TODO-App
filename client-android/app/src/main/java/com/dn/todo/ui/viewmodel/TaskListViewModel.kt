package com.dn.todo.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dn.todo.domain.TaskManager
import com.dn.todo.ui.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val taskManager: TaskManager,
    app: Application
): AndroidViewModel(app) {

    private val context: Context get() = getApplication()

    val tasks = taskManager
        .getTasks()
        .cachedIn(viewModelScope)

    private val _event = MutableSharedFlow<DataDrivenEvent>()
    val event = _event.asSharedFlow()

    fun markTask(taskId: Long, isCompleted: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                taskManager.setTaskCompleted(taskId, isCompleted)
            }
            _event.emit(DataDrivenEvent.RefreshTaskList)
        }
    }

    fun editTask(taskId: Long) {
        viewModelScope.launch {
            _event.emit(DataDrivenEvent.NavigateToTaskEdit(taskId))
        }
    }

    fun createTask() {
        viewModelScope.launch {
            val task = withContext(Dispatchers.IO) {
                taskManager.createTask(
                    context.getString(R.string.task_title_default)
                    , null
                )
            }
            _event.emit(DataDrivenEvent.NavigateToTaskEdit(task.id))
        }
    }

    fun deleteTask(taskId: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                taskManager.deleteTask(taskId)
            }
            _event.emit(DataDrivenEvent.RefreshTaskList)
        }
    }

    sealed interface DataDrivenEvent {
        data object RefreshTaskList: DataDrivenEvent
        data class NavigateToTaskEdit(val taskId: Long): DataDrivenEvent
    }
}