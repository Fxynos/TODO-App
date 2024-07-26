package com.dn.todo.domain

interface TaskRepository {
    fun get(id: Long): Task
    fun getAll(limit: Int, fromId: Long?): List<Task>
    fun insert(task: Task): Long
    fun update(task: Task)
    fun delete(id: Long)
}