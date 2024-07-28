package com.dn.todo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dn.todo.domain.Task
import com.dn.todo.domain.TaskManager
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
        viewModelScope.launch {
            val task = withContext(Dispatchers.IO) { taskManager.getTask(taskId) }
            _uiState.emit(UiState(task.title, task.description ?: "", task.isCompleted))
        }
    }

    fun setTitle(title: String) = _uiState.update { it.copy(title = title) }
    fun setDescription(description: String) = _uiState.update { it.copy(description = description) }
    fun setCompleted(isCompleted: Boolean) = _uiState.update { it.copy(isCompleted = isCompleted) }

    fun saveTask() {
        if (!uiState.value.areInputsValid)
            return

        val state = uiState.value
        val finalTitle = state.title.trim()
        val finalDescription = if (state.description.isBlank()) null else state.description.trim()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                taskManager.updateTask(Task(taskId, finalTitle, finalDescription, state.isCompleted))
            }
            _event.emit(DataDrivenEvent.NavigateBack)
        }
    }

    fun deleteTask() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { taskManager.deleteTask(taskId) }
            _event.emit(DataDrivenEvent.NavigateBack)
        }
    }

    sealed interface DataDrivenEvent {
        data object NavigateBack: DataDrivenEvent
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