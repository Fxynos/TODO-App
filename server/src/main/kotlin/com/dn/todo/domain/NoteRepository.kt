package com.dn.todo.domain

interface NoteRepository {
    fun get(id: Long): Note
    fun getAll(limit: Int, fromId: Long): List<Note>
    fun insert(note: Note): Int
    fun update(note: Note)
    fun delete(id: Long)
}