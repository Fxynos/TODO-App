package com.dn.todo.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.dn.todo.domain.TaskManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val taskManager: TaskManager
): ViewModel() {
    // TODO
}