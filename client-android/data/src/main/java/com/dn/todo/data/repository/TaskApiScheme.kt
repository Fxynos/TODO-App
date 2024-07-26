package com.dn.todo.data.repository

import com.dn.todo.domain.Task
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

internal interface TaskApiScheme {
    @GET("tasks")
    fun getAll(
        @Query("limit") limit: Int,
        @Query("from_id") fromId: Long?
    ): Call<List<Task>>

    /**
     * @param task MUST have its [Task.id] and [Task.isCompleted] equal `null`
     */
    @POST("tasks")
    fun create(@Body task: TaskDto): Call<Nothing>

    @PUT("tasks/{id}")
    fun update(
        @Body task: TaskDto,
        @Path("id") id: Long
    ): Call<Nothing>

    @DELETE("tasks/{id}")
    fun delete(@Path("id") id: Long): Call<Nothing>
}