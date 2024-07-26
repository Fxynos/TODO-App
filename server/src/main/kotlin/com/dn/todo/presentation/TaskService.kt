package com.dn.todo.presentation

import com.dn.todo.data.dto.TaskDto
import com.dn.todo.data.dto.toDto
import com.dn.todo.data.dto.toEntity
import com.dn.todo.domain.Task
import com.dn.todo.domain.TaskRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TaskService(@Autowired private val repository: TaskRepository) {

    fun getTasksPage(limit: Int, fromId: Long?): List<TaskDto> = repository.getAll(limit, fromId).map(Task::toDto)

    fun createTask(note: TaskDto): Long = repository.insert(note.toEntity())

    fun updateTask(note: TaskDto) {
        repository.update(note.toEntity())
    }

    fun deleteTask(id: Long) {
        repository.delete(id)
    }
}