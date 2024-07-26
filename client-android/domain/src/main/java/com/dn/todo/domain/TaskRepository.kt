package com.dn.todo.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getAll(): Flow<PagingData<Task>>
    fun create(task: Task)
    fun update(task: Task)
    fun delete(id: Long)
}