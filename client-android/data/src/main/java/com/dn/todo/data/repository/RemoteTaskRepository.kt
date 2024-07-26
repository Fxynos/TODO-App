package com.dn.todo.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dn.todo.data.paging.TaskPagingSource
import com.dn.todo.domain.Task
import com.dn.todo.domain.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "http://192.168.0.10:8080/" // TODO move to build config
private const val TAG = "RemoteTaskRepo"

class RemoteTaskRepository(timeoutMs: Long): TaskRepository {

    private val api: TaskApiScheme = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(
            OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val method = chain.request().method()
                    val path = chain.request().url().encodedPath()
                    val ms = System.currentTimeMillis()
                    val response = chain.proceed(chain.request()) // actually makes request here
                    Log.d(
                        TAG,
                        "${response.code()} $method $path (${System.currentTimeMillis() - ms} ms)"
                    )
                    response
                }.callTimeout(timeoutMs, TimeUnit.MILLISECONDS)
                .build()
        ).build()
        .create(TaskApiScheme::class.java)

    private fun getAll(limit: Int, fromId: Long?): List<Task> =
        api.getAll(limit, fromId).execute().getOrThrow()

    /**
     * @return flow that SHOULD be [Flow.cachedIn] in corresponding [CoroutineScope]
     */
    override fun getAll(): Flow<PagingData<Task>> = Pager(
        config = PagingConfig(10),
        pagingSourceFactory = {
            TaskPagingSource { limit, fromId ->
                val items = getAll(limit, fromId)
                items to if (items.size < limit) null else items.last().id
            }
        }
    ).flow

    override fun create(task: Task) =
        api.create(TaskDto().apply {
            title = task.title
            description = task.description
        }).execute().assertSuccess()

    override fun update(task: Task) =
        api.update(TaskDto().apply {
            title = task.title
            description = task.description
            isCompleted = task.isCompleted
        }, task.id).execute().assertSuccess()

    override fun delete(id: Long) =
        api.delete(id).execute().assertSuccess()

    private fun <T> Response<T>.assertSuccess() {
        if (!isSuccessful)
            throw TaskApiException(this)
    }

    private fun <T> Response<T>.getOrThrow() =
        if (isSuccessful)
            body()!!
        else throw TaskApiException(this)
}