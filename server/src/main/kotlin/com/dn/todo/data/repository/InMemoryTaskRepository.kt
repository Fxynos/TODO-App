package com.dn.todo.data.repository

import com.dn.todo.domain.Task
import com.dn.todo.domain.TaskRepository
import java.util.*
import java.util.concurrent.atomic.AtomicLong

class InMemoryTaskRepository: TaskRepository { // temp mock repo impl
    companion object {
        private var idCounter = AtomicLong(1L)
    }

    private val cache = TreeMap<Long, Task>()

    override fun get(id: Long): Task = cache[id]!!

    override fun getAll(limit: Int, fromId: Long?): List<Task> =
        cache.entries
            .asSequence().run {
                if (fromId == null)
                    this
                else
                    dropWhile { it.key <= fromId }
            }.take(limit)
            .map(Map.Entry<Long, Task>::value)
            .toList()

    override fun insert(task: Task): Long {
        val id = idCounter.getAndIncrement()
        cache[id] = task.copy(id = id)
        return id
    }

    override fun update(task: Task) {
        if (!cache.contains(task.id))
            throw IllegalArgumentException("No such note")

        cache[task.id] = task
    }

    override fun delete(id: Long) {
        cache.remove(id)
    }
}