package com.dn.todo.domain

import java.util.*
import java.util.concurrent.atomic.AtomicLong

private var idCounter = AtomicLong(1L)

class InMemoryNoteRepository: NoteRepository { // temp mock repo impl

    private val cache = TreeMap<Long, Note>()

    override fun get(id: Long): Note = cache[id]!!

    override fun getAll(limit: Int, fromId: Long): List<Note> =
        cache.entries
            .asSequence()
            .dropWhile { it.key <= fromId }
            .take(limit)
            .map(Map.Entry<Long, Note>::value)
            .toList()

    override fun insert(note: Note): Long {
        val id = idCounter.getAndIncrement()
        cache[id] = note.copy(id = id)
        return id
    }

    override fun update(note: Note) {
        if (!cache.contains(note.id))
            throw IllegalArgumentException("No such note")

        cache[note.id] = note
    }

    override fun delete(id: Long) {
        cache.remove(id)
    }
}