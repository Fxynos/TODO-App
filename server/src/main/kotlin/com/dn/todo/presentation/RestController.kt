package com.dn.todo.presentation

import com.dn.todo.data.dto.TaskDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class RestController(@Autowired private val service: TaskService) {

    @GetMapping("/tasks")
    fun getTasks(
        @RequestParam(defaultValue = "10") limit: Int,
        @RequestParam("from_id", required = false) fromId: Long?,
    ): ResponseEntity<List<TaskDto>> {
        if (fromId != null && fromId < 0L)
            return ResponseEntity(HttpStatus.BAD_REQUEST)

        return ResponseEntity.ok(
            service.getTasksPage(limit, fromId)
        )
    }

    /**
     * Accepts task:
     * ```json
     * {
     *  "title": "Custom name",
     *  "description": "Custom optional description"
     * }
     * ```
     */
    @PostMapping("/tasks")
    fun createTask(@Valid @RequestBody task: TaskDto): ResponseEntity<Nothing> {
        if (task.isCompleted != null || task.id != null || !isValid(task))
            return ResponseEntity(HttpStatus.BAD_REQUEST)

        service.createTask(task)

        return ResponseEntity(HttpStatus.OK)
    }

    @PutMapping("/tasks/{id}")
    fun updateTask(
        @PathVariable id: Long,
        @Valid @RequestBody task: TaskDto
    ): ResponseEntity<Nothing> {
        if (!isValid(task))
            return ResponseEntity(HttpStatus.BAD_REQUEST)

        service.updateTask(task.copy(id = id))

        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/tasks/{id}")
    fun deleteTask(@PathVariable id: Long): ResponseEntity<Nothing> {
        service.deleteTask(id)
        return ResponseEntity(HttpStatus.OK)
    }

    private fun isValid(task: TaskDto): Boolean =
        task.id == null && task.title.isNotBlank() && task.description?.isNotBlank() ?: true
}