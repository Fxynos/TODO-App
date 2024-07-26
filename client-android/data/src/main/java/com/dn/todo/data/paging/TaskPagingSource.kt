package com.dn.todo.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dn.todo.domain.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class TaskPagingSource(
    private val fetch: (pageSize: Int, key: Long?) -> Pair<List<Task>, Long?>
): PagingSource<Long, Task>() {
    override fun getRefreshKey(state: PagingState<Long, Task>): Long? = null

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Task> =
        withContext(Dispatchers.IO) {
            val (items, nextKey) = fetch(params.loadSize, params.key)
            LoadResult.Page(items, null, nextKey)
        }
}