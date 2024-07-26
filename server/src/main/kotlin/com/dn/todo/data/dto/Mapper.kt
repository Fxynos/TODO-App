package com.dn.todo.data.dto

import com.dn.todo.domain.Task

fun TaskDto.toEntity() =
    Task(id ?: Task.ID_NONE, title, description, isCompleted ?: false)

fun Task.toDto() =
    TaskDto(id.takeUnless { it == Task.ID_NONE }, title, description, isCompleted)