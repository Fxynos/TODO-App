package com.dn.todo.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

class TaskManager(private val repository: TaskRepository) {

    fun createTask(title: String, description: String?): Task =
        repository.create(Task(-1, title, description, false))

    fun getTask(taskId: Long): Task = repository.get(taskId)

    fun getTasks(): Flow<PagingData<Task>> = repository.getAll()

    fun deleteTask(taskId: Long): Unit = repository.delete(taskId)

    fun updateTask(task: Task): Unit = repository.update(task)

    fun setTaskCompleted(taskId: Long, isCompleted: Boolean): Unit =
        repository.update(
            getTask(taskId).copy(isCompleted = isCompleted)
        )

    fun setTaskTitle(taskId: Long, title: String): Unit =
        repository.update(
            getTask(taskId).copy(title = title)
        )

    fun setTaskDescription(taskId: Long, description: String?): Unit =
        repository.update(
            getTask(taskId).copy(description = description)
        )
}