package com.dn.todo.domain

data class Task(
    val id: Int,
    val title: String,
    val description: String?,
    val isCompleted: Boolean
)