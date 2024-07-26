package com.dn.todo.data

import com.dn.todo.domain.Note
import com.dn.todo.domain.NoteRepository
import org.springframework.stereotype.Repository

@Repository
class H2NoteRepository: NoteRepository {

    override fun get(id: Long): Note {
        TODO("Not yet implemented")
    }

    override fun getAll(limit: Int, fromId: Long): List<Note> {
        TODO("Not yet implemented")
    }

    override fun insert(note: Note): Long {
        TODO("Not yet implemented")
    }

    override fun update(note: Note) {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }
}