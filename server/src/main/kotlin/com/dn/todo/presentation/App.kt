package com.dn.todo.presentation

import com.dn.todo.data.H2NoteRepository
import com.dn.todo.domain.NoteRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean

@SpringBootApplication
abstract class App {
    companion object {
        @JvmStatic
        fun main(vararg args: String) {
            SpringApplicationBuilder(App::class.java).run(*args)
        }
    }

    @Bean
    abstract fun bindNoteRepository(repository: H2NoteRepository): NoteRepository
}