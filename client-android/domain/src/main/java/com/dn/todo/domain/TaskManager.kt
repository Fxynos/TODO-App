package com.dn.todo.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

class TaskManager(private val repository: TaskRepository) {

    fun createTask(title: String, description: String?): Boolean {
        val task = Task(-1, title, description, false)
        if (!task.isValid)
            return false

        repository.create(task)
        return true
    }

    fun getTasks(): Flow<PagingData<Task>> = repository.getAll()

    fun deleteTask(taskId: Long): Unit = repository.delete(taskId)

    fun updateTask(task: Task): Boolean {
        if (!task.isValid)
            return false

        repository.update(task)
        return true
    }

    private val Task.isValid: Boolean
        get() = title.isNotBlank() && description?.isNotBlank() ?: true
}