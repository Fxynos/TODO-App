package com.dn.todo.data.repository

import com.dn.todo.domain.Task
import com.dn.todo.domain.TaskRepository
import org.springframework.stereotype.Repository

@Repository
class H2TaskRepository: TaskRepository {

    override fun get(id: Long): Task {
        TODO("Not yet implemented")
    }

    override fun getAll(limit: Int, fromId: Long?): List<Task> {
        TODO("Not yet implemented")
    }

    override fun insert(task: Task): Long {
        TODO("Not yet implemented")
    }

    override fun update(task: Task) {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }
}