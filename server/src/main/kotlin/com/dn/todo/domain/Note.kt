package com.dn.todo.domain

data class Note(
    val id: Long = ID_NONE,
    val title: String,
    val description: String?,
    val isCompleted: Boolean
) {
    companion object {
        const val ID_NONE = -1L
    }
}