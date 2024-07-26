package com.dn.todo.domain

data class Task(
    val id: Long,
    val title: String,
    val description: String?,
    val isCompleted: Boolean
)