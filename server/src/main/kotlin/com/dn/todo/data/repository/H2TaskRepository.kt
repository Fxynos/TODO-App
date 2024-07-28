package com.dn.todo.data.repository

import com.dn.todo.domain.Task
import com.dn.todo.domain.TaskRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Repository
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.util.LinkedList

@Primary
@Repository
class H2TaskRepository(@Value("\${db.h2.address}") address: String): TaskRepository {
    companion object {
        init { Class.forName("org.h2.Driver") }
    }

    private val connection = DriverManager.getConnection(address) // embedded db doesn't require credentials

    override fun get(id: Long): Task =
        connection.prepareStatement("select * from task where id = ?;").run {
            setLong(1, id)
            executeQuery().also(ResultSet::next).getTask()
        }

    override fun getAll(limit: Int, fromId: Long?): List<Task> =
        connection.prepareStatement("select * from task where id > ? limit ?;").run {
            setLong(1, fromId ?: -1)
            setInt(2, limit)

            executeQuery().run {
                val list = LinkedList<Task>()
                while (next())
                    list += getTask()
                list
            }
        }

    override fun insert(task: Task): Long =
        connection.prepareStatement(
            "insert into task (title, description, completed) values (?, ?, ?);",
            Statement.RETURN_GENERATED_KEYS
        ).run {
            setString(1, task.title)
            setString(2, task.description)
            setBoolean(3, task.isCompleted)
            execute()

            generatedKeys
                .also(ResultSet::next)
                .getLong("id")
        }

    override fun update(task: Task) = // fail-safe
        connection.prepareStatement(
            "update task set title = ?, description = ?, completed = ? where id = ?;"
        ).run {
            setString(1, task.title)
            setString(2, task.description)
            setBoolean(3, task.isCompleted)
            setLong(4, task.id)
            execute()
            Unit
        }

    override fun delete(id: Long) = // fail-safe
        connection.prepareStatement("delete from task where id = ?;").run {
            setLong(1, id)
            execute()
            Unit
        }

    private fun ResultSet.getTask() = Task(
        getLong("id"),
        getString("title"),
        getString("description"),
        getBoolean("completed")
    )
}