package com.dn.todo.data

import com.dn.todo.domain.Note

fun NoteDto.toEntity() =
    Note(id ?: Note.ID_NONE, title, description, isCompleted ?: false)

fun Note.toDto() =
    NoteDto(id.takeUnless { it == Note.ID_NONE }, title, description, isCompleted)