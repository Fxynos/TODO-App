package com.dn.todo.data.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class TaskDto(
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: Long?,
    val title: String,
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("is_completed")
    @get:JsonProperty("is_completed")
    val isCompleted: Boolean?
)