package com.dn.todo.presentation

import com.dn.todo.data.repository.InMemoryTaskRepository
import com.dn.todo.domain.TaskRepository
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
    abstract fun bindNoteRepository(repository: InMemoryTaskRepository): TaskRepository // TODO implement H2 DB
}