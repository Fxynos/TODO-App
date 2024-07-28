package com.dn.todo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dn.todo.domain.Task
import com.dn.todo.domain.TaskManager
import com.dn.todo.ui.viewmodel.TaskListViewModel.DataDrivenEvent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

@HiltViewModel(assistedFactory = TaskEditViewModel.Factory::class)
class TaskEditViewModel @AssistedInject constructor(
    private val taskManager: TaskManager,
    @Assisted private val taskId: Long
): ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(taskId: Long): TaskEditViewModel
    }

    private val _event = MutableSharedFlow<DataDrivenEvent>()
    val event = _event.asSharedFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        catchConnectionError {
            val task = withContext(Dispatchers.IO) { taskManager.getTask(taskId) }
            _uiState.emit(UiState(task.title, task.description ?: "", task.isCompleted))
        }
    }

    fun setTitle(title: String) =
        _uiState.update { it.copy(title = title.trimToSize(30)) }
    fun setDescription(description: String) =
        _uiState.update { it.copy(description = description.trimToSize(1000)) }
    fun setCompleted(isCompleted: Boolean) =
        _uiState.update { it.copy(isCompleted = isCompleted) }

    fun saveTask() {
        if (!uiState.value.areInputsValid)
            return

        val state = uiState.value
        val finalTitle = state.title.trim()
        val finalDescription = if (state.description.isBlank()) null else state.description.trim()

        catchConnectionError {
            withContext(Dispatchers.IO) {
                taskManager.updateTask(Task(taskId, finalTitle, finalDescription, state.isCompleted))
            }
            _event.emit(DataDrivenEvent.NavigateBack)
        }
    }

    fun deleteTask() {
        catchConnectionError {
            withContext(Dispatchers.IO) { taskManager.deleteTask(taskId) }
            _event.emit(DataDrivenEvent.NavigateBack)
        }
    }

    private inline fun catchConnectionError(crossinline block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                block()
            } catch (e: IOException) {
                _event.emit(DataDrivenEvent.NotifyNoConnection)
            }
        }
    }

    private fun String.trimToSize(size: Int) = if (length > size) substring(0, size) else this

    sealed interface DataDrivenEvent {
        data object NavigateBack: DataDrivenEvent
        data object NotifyNoConnection: DataDrivenEvent
    }

    data class UiState(
        val title: String = "",
        val description: String = "",
        val isCompleted: Boolean = false
    ) {
        val isTitleValid: Boolean = title.isNotBlank()
        val isDescriptionValid: Boolean = description.length <= 1000
        val areInputsValid: Boolean get() = isTitleValid && isDescriptionValid
    }
}