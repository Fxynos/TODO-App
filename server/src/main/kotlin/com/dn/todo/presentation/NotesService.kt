package com.dn.todo.presentation

import com.dn.todo.data.NoteDto
import com.dn.todo.data.toDto
import com.dn.todo.data.toEntity
import com.dn.todo.domain.Note
import com.dn.todo.domain.NoteRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class NotesService(@Autowired private val repository: NoteRepository) {

    fun getNote(id: Long): NoteDto = repository.get(id).toDto()

    fun getNotesPage(limit: Int, fromId: Long): List<NoteDto> = repository.getAll(limit, fromId).map(Note::toDto)

    fun createNote(note: NoteDto) {
        repository.insert(note.toEntity())
    }

    fun updateNote(note: NoteDto) {
        repository.update(note.toEntity())
    }

    fun deleteNote(id: Long) {
        repository.delete(id)
    }
}