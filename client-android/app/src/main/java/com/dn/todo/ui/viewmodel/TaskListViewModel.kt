package com.dn.todo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dn.todo.domain.TaskManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val taskManager: TaskManager
): ViewModel() {
    val tasks = taskManager
        .getTasks()
        .cachedIn(viewModelScope)

    private val _refreshEvent = MutableSharedFlow<Nothing?>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )
    val refreshEvent = _refreshEvent.asSharedFlow()

    fun markTask(taskId: Long, isCompleted: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                taskManager.setTaskCompleted(taskId, isCompleted)
            }
            _refreshEvent.emit(null)
        }
    }
}