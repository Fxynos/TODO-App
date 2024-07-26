package com.dn.todo.data

import com.fasterxml.jackson.annotation.JsonInclude

class NoteDto(
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: Long?,
    val title: String,
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val isCompleted: Boolean?
)